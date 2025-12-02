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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private PromoCodeRepository promoCodeRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Client client;
    private Product product;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setTotalOrders(0);
        client.setTotalSpent(BigDecimal.ZERO);
        client.setFidelityLevel(null);

        product = new Product();
        product.setId(1L);
        product.setName("TestProd");
        product.setPrice(new BigDecimal("100.00"));
        product.setStockQuantity(10);
        product.setDeleted(false);

        ReflectionTestUtils.setField(orderService, "taxRate", new BigDecimal("0.20"));
    }

    @Test
    void createOrder_successful() {
        OrderDto dto = new OrderDto();
        dto.setClientId(1L);
        dto.setOrderDate(LocalDateTime.now());
        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setProductId(1L);
        itemDto.setQuantity(2);
        List<OrderItemDto> items = new ArrayList<>();
        items.add(itemDto);
        dto.setItems(items);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));

        Order savedOrder = new Order();
        savedOrder.setId(10L);
        savedOrder.setClient(client);
        savedOrder.setItems(new ArrayList<>());
        savedOrder.setSubtotal(new BigDecimal("200.00"));
        savedOrder.setDiscount(BigDecimal.ZERO);
        savedOrder.setTva(new BigDecimal("40.00"));
        savedOrder.setTotal(new BigDecimal("240.00"));
        savedOrder.setRemainingAmount(new BigDecimal("240.00"));
        savedOrder.setStatus(OrderStatus.PENDING);

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.toDto(any(Order.class))).thenReturn(dto);

        OrderDto result = orderService.createOrder(dto);

        assertThat(result).isNotNull();
        verify(productRepository).findByIdAndDeletedFalse(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_missingClient_throws() {
        OrderDto dto = new OrderDto();
        dto.setClientId(null);
        assertThrows(BusinessRuleException.class, () -> orderService.createOrder(dto));
    }

    @Test
    void createOrder_emptyItems_throws() {
        OrderDto dto = new OrderDto();
        dto.setClientId(1L);
        dto.setItems(new ArrayList<>());
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        assertThrows(BusinessRuleException.class, () -> orderService.createOrder(dto));
    }

    @Test
    void confirmOrder_successful() {
        Order order = new Order();
        order.setId(5L);
        order.setStatus(OrderStatus.PENDING);
        order.setClient(client);
        order.setItems(new ArrayList<>());
        order.setPayments(new ArrayList<>());
        order.setRemainingAmount(new BigDecimal("0.00"));
        order.setTotal(new BigDecimal("0.00"));

        when(orderRepository.findById(5L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto());

        OrderDto res = orderService.confirmOrder(5L);

        assertThat(res).isNotNull();
        verify(clientRepository).save(any(Client.class));
        verify(productRepository, atLeast(0)).save(any(Product.class));
    }

    @Test
    void confirmOrder_notFound_throws() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.confirmOrder(99L));
    }

    @Test
    void cancelOrder_successful() {
        Order order = new Order();
        order.setId(6L);
        order.setStatus(OrderStatus.PENDING);
        when(orderRepository.findById(6L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto());

        OrderDto res = orderService.cancelOrder(6L);
        assertThat(res).isNotNull();
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void cancelOrder_confirmed_throws() {
        Order order = new Order();
        order.setId(7L);
        order.setStatus(OrderStatus.CONFIRMED);
        when(orderRepository.findById(7L)).thenReturn(Optional.of(order));
        assertThrows(BusinessRuleException.class, () -> orderService.cancelOrder(7L));
    }

    @Test
    void getOrderById_notFound_throws() {
        when(orderRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(100L));
    }

}
