package com.smartshop.smartshop.service.impl;

import com.smartshop.smartshop.dto.OrderDto;
import com.smartshop.smartshop.dto.OrderItemDto;
import com.smartshop.smartshop.entity.*;
import com.smartshop.smartshop.entity.enums.CustomerTier;
import com.smartshop.smartshop.entity.enums.OrderStatus;
import com.smartshop.smartshop.entity.enums.PaymentStatus;
import com.smartshop.smartshop.exception.BusinessRuleException;
import com.smartshop.smartshop.exception.ResourceNotFoundException;
import com.smartshop.smartshop.mapper.OrderItemMapper;
import com.smartshop.smartshop.mapper.OrderMapper;
import com.smartshop.smartshop.repository.*;
import com.smartshop.smartshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;

    @Value("${app.tax.rate:0.20}")
    private BigDecimal taxRate;

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {

        if (orderDto.getClientId() == null) {
            throw new com.smartshop.smartshop.exception.BusinessRuleException("Client ID must be provided");
        }
        Client client = clientRepository.findById(orderDto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + orderDto.getClientId()));

        if (orderDto.getItems() == null || orderDto.getItems().isEmpty()) {
            throw new BusinessRuleException("Order must contain at least one item");
        }

        Order order = new Order();
        order.setClient(client);
        order.setOrderDate(orderDto.getOrderDate() != null ? orderDto.getOrderDate() : LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (OrderItemDto itemDto : orderDto.getItems()) {
            Product product = productRepository.findByIdAndDeletedFalse(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDto.getProductId()));

            if (product.getDeleted() != null && product.getDeleted()) {
                order.setStatus(OrderStatus.REJECTED);
                order.setRemainingAmount(BigDecimal.ZERO);
                orderRepository.save(order);
                throw new BusinessRuleException("Produit supprimé : " + product.getName());
            }

            if (itemDto.getQuantity() == null || itemDto.getQuantity() <= 0) {
                throw new BusinessRuleException("Quantity must be at least 1 for product id: " + product.getId());
            }

            if (product.getStockQuantity() < itemDto.getQuantity()) {
                order.setStatus(OrderStatus.REJECTED);
                order.setRemainingAmount(BigDecimal.ZERO);
                orderRepository.save(order);
                throw new BusinessRuleException("Insufficient stock for product id: " + product.getId());
            }

            BigDecimal linePrice = product.getPrice();
            BigDecimal totalLine = linePrice.multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            subtotal = subtotal.add(totalLine);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(linePrice);
            orderItem.setTotalLine(totalLine);
            orderItem.setOrder(order);
            items.add(orderItem);
        }
//
//        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
//        order.setSubtotal(subtotal);
//
//        BigDecimal discount = BigDecimal.ZERO;
//
//        if (client.getFidelityLevel() != null) {
//            BigDecimal fidelityPercent = BigDecimal.ZERO;
//            switch (client.getFidelityLevel()) {
//                case SILVER:
//                    fidelityPercent = BigDecimal.valueOf(0.05);
//                    break;
//                case GOLD:
//                    fidelityPercent = BigDecimal.valueOf(0.10);
//                    break;
//                case PLATINUM:
//                    fidelityPercent = BigDecimal.valueOf(0.15);
//                    break;
//                default:
//                    fidelityPercent = BigDecimal.ZERO;
//            }
//            discount = discount.add(subtotal.multiply(fidelityPercent));
//        }

        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        order.setSubtotal(subtotal);

        BigDecimal discount = BigDecimal.ZERO;

        if (client.getFidelityLevel() != null) {

            BigDecimal fidelityPercent = BigDecimal.ZERO;

            switch (client.getFidelityLevel()) {
                case SILVER:
                    if (subtotal.compareTo(BigDecimal.valueOf(500)) >= 0) {
                        fidelityPercent = BigDecimal.valueOf(0.05);
                    }
                    break;

                case GOLD:
                    if (subtotal.compareTo(BigDecimal.valueOf(800)) >= 0) {
                        fidelityPercent = BigDecimal.valueOf(0.10);
                    }
                    break;

                case PLATINUM:
                    if (subtotal.compareTo(BigDecimal.valueOf(1200)) >= 0) {
                        fidelityPercent = BigDecimal.valueOf(0.15);
                    }
                    break;

                default:
                    fidelityPercent = BigDecimal.ZERO;
            }

            discount = subtotal.multiply(fidelityPercent)
                    .setScale(2, RoundingMode.HALF_UP);
        }


        if (orderDto.getPromoCode() != null && !orderDto.getPromoCode().isBlank()) {
            PromoCode promo = promoCodeRepository
                    .findByCodeAndAvailabilityStatusFalse(orderDto.getPromoCode())
                    .orElseThrow(() -> new BusinessRuleException("Invalid or unavailable promo code"));


            BigDecimal promoValue = subtotal.multiply(BigDecimal.valueOf(0.05));
            discount = discount.add(promoValue);
            order.setPromoCode(promo);
            promo.setOrder(order);

            promo.setAvailabilityStatus(Boolean.TRUE);
            promoCodeRepository.save(promo);
        } else {
            order.setPromoCode(null);
        }

        discount = discount.setScale(2, RoundingMode.HALF_UP);
        order.setDiscount(discount);

        BigDecimal amountAfterDiscount = subtotal.subtract(discount).setScale(2, RoundingMode.HALF_UP);

        BigDecimal tva = amountAfterDiscount.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
        order.setTva(tva);

        BigDecimal total = amountAfterDiscount.add(tva).setScale(2, RoundingMode.HALF_UP);
        order.setTotal(total);

        order.setRemainingAmount(total);

        order.setItems(items);

        if (order.getClient() == null) {
            throw new BusinessRuleException("Order client is null - cannot persist order without client");
        }
        order.setPayments(new ArrayList<>());
        Order saved = orderRepository.save(order);

        for (OrderItem it : saved.getItems()) {
            orderItemRepository.save(it);
        }

        return orderMapper.toDto(saved);
    }

    @Override
    @Transactional
    public OrderDto confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessRuleException("Only PENDING orders can be confirmed");
        }

        BigDecimal paid = BigDecimal.ZERO;
        if (order.getPayments() != null) {
            for (var p : order.getPayments()) {
                if (p.getStatus() != null && p.getStatus().equals(PaymentStatus.ENCAISSÉ)) {
                    paid = paid.add(p.getAmountPaid() != null ? p.getAmountPaid() : BigDecimal.ZERO);
                }
            }
        }
        if (paid.compareTo(order.getTotal()) < 0) {
            throw new BusinessRuleException("Payment incomplete for order id: " + orderId);
        }

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findByIdAndDeletedFalse(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + item.getProduct().getId()));

            if (product.getStockQuantity() < item.getQuantity()) {
                order.setStatus(OrderStatus.REJECTED);
                orderRepository.save(order);
                throw new BusinessRuleException("Insufficient stock for product id: " + product.getId());
            }

            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        Client client = order.getClient();
        client.setTotalOrders(client.getTotalOrders() + 1);
        client.setTotalSpent(client.getTotalSpent().add(order.getTotal()));

        BigDecimal spent = client.getTotalSpent();
        if (spent.compareTo(BigDecimal.valueOf(15000)) >= 0 || client.getTotalOrders() >= 20) {
            client.setFidelityLevel(CustomerTier.PLATINUM);
        } else if (spent.compareTo(BigDecimal.valueOf(5000)) >= 0 || client.getTotalOrders() >= 10) {
            client.setFidelityLevel(CustomerTier.GOLD);
        } else if (spent.compareTo(BigDecimal.valueOf(1000)) >= 0 || client.getTotalOrders() >= 3) {
            client.setFidelityLevel(CustomerTier.SILVER);
        } else {
            client.setFidelityLevel(CustomerTier.BASIC);
        }
        clientRepository.save(client);

        order.setStatus(OrderStatus.CONFIRMED);
        order.setRemainingAmount(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        Order saved = orderRepository.save(order);

        return orderMapper.toDto(saved);
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new BusinessRuleException("Cannot cancel a confirmed order via this method");
        }

        order.setStatus(OrderStatus.CANCELED);
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getOrdersByClientId(Long clientId) {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .filter(o -> o.getClient() != null && o.getClient().getId().equals(clientId))
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
}