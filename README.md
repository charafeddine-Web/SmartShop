# SmartShop - Système de Gestion Commerciale B2B

## 📋 Description du Projet

SmartShop est une **API REST backend** développée pour **MicroTech Maroc**, distributeur B2B de matériel informatique basé à Casablanca. L'application permet de gérer un portefeuille de 650 clients actifs avec un système de fidélité automatique, des paiements fractionnés multi-moyens, et une traçabilité complète des transactions financières.

> ⚠️ **Important** : Cette application est **purement backend** (pas d'interface graphique). Les tests se font via **Postman** ou **Swagger**.

---

## 🎯 Objectifs du Projet

- Gérer efficacement les clients B2B et leur historique commercial
- Automatiser le système de fidélité avec remises progressives
- Permettre des paiements fractionnés avec plusieurs moyens de paiement
- Assurer une traçabilité complète des événements financiers
- Optimiser la gestion de trésorerie

---

## 🛠️ Technologies Utilisées

### Backend
- **Java 8+**
- **Spring Boot** (Framework principal)
- **Spring Data JPA** (ORM Hibernate)
- **Spring Web** (API REST)

### Base de Données
- **PostgreSQL** ou **MySQL**

### Outils & Librairies
- **Lombok** - Réduction du code boilerplate
- **MapStruct** - Mapping Entity ↔ DTO
- **JUnit 5** - Tests unitaires
- **Mockito** - Mocking pour les tests
- **Swagger/OpenAPI** - Documentation API
- **Maven** - Gestion des dépendances

### Authentification
- **HTTP Session** (login/logout simple)
- ❌ Pas de JWT ni Spring Security

---

## 🏗️ Architecture

### Pattern en Couches

```
┌─────────────────────────────────────┐
│         Controller Layer            │  ← API REST Endpoints
├─────────────────────────────────────┤
│          Service Layer              │  ← Logique métier
├─────────────────────────────────────┤
│        Repository Layer             │  ← Accès aux données
├─────────────────────────────────────┤
│          Entity Layer               │  ← Modèles de données
└─────────────────────────────────────┘
         ↕ MapStruct
┌─────────────────────────────────────┐
│            DTO Layer                │  ← Transfert de données
└─────────────────────────────────────┘
```

### Gestion Centralisée
- **@ControllerAdvice** pour la gestion des exceptions
- **Validation** avec annotations (@Valid, @NotNull, etc.)
- **Interfaces** pour une meilleure abstraction

---

## 📊 Modèle de Données

### Entités Principales

#### **User**
```java
- id (Long)
- username (String)
- password (String)
- role (UserRole: ADMIN | CLIENT)
```

#### **Client**
```java
- id (Long)
- nom (String)
- email (String)
- tier (CustomerTier)
- totalOrders (Integer) - Calculé automatiquement
- totalSpent (BigDecimal) - Calculé automatiquement
- firstOrderDate (LocalDateTime)
- lastOrderDate (LocalDateTime)
```

#### **Product**
```java
- id (Long)
- nom (String)
- prixUnitaire (BigDecimal)
- stock (Integer)
- deleted (Boolean) - Soft delete
```

#### **Order (Commande)**
```java
- id (Long)
- client (Client)
- orderItems (List<OrderItem>)
- dateCommande (LocalDateTime)
- sousTotal (BigDecimal)
- montantRemise (BigDecimal)
- montantHT (BigDecimal)
- tva (BigDecimal)
- totalTTC (BigDecimal)
- codePromo (String)
- status (OrderStatus)
- montantRestant (BigDecimal)
```

#### **OrderItem**
```java
- id (Long)
- order (Order)
- product (Product)
- quantite (Integer)
- prixUnitaire (BigDecimal)
- totalLigne (BigDecimal)
```

#### **Payment (Paiement)**
```java
- id (Long)
- order (Order)
- numeroPaiement (Integer)
- montant (BigDecimal)
- typePaiement (PaymentType)
- datePaiement (LocalDateTime)
- dateEncaissement (LocalDateTime)
- status (PaymentStatus)
- reference (String)
- banque (String)
- dateEcheance (LocalDate)
```

---

## 🔐 Système d'Authentification & Permissions

### Rôles Utilisateurs

| Rôle | Description | Permissions |
|------|-------------|-------------|
| **ADMIN** | Employé MicroTech | CRUD complet, validation commandes, gestion totale |
| **CLIENT** | Entreprise cliente | Consultation uniquement (profil, commandes, produits) |

### Matrice de Permissions

| Action | CLIENT | ADMIN |
|--------|--------|-------|
| Consulter son profil | ✅ | ✅ |
| Consulter ses commandes | ✅ | ✅ |
| Consulter tous les clients | ❌ | ✅ |
| Créer une commande | ❌ | ✅ |
| Valider/Annuler commande | ❌ | ✅ |
| Gérer les produits | ❌ | ✅ |
| Gérer les paiements | ❌ | ✅ |

---

## 💎 Système de Fidélité Automatique

### Niveaux de Fidélité

| Niveau | Conditions d'obtention | Remise | Seuil minimum |
|--------|------------------------|--------|---------------|
| **BASIC** | Par défaut | 0% | - |
| **SILVER** | 3 commandes OU 1,000 DH | 5% | 500 DH |
| **GOLD** | 10 commandes OU 5,000 DH | 10% | 800 DH |
| **PLATINUM** | 20 commandes OU 15,000 DH | 15% | 1,200 DH |

### Fonctionnement

1. **Acquisition du niveau** : Basé sur l'historique total (commandes + montant cumulé)
2. **Utilisation du niveau** : Remise appliquée sur les futures commandes si seuil atteint
3. **Mise à jour** : Automatique après chaque commande confirmée

### Exemple Pratique

```
Client Amine s'inscrit → BASIC

Commande 1: 250 DH → Confirmée → 1 commande, 250 DH cumulé → BASIC
Commande 2: 350 DH → Confirmée → 2 commandes, 600 DH cumulé → BASIC
Commande 3: 450 DH → Confirmée → 3 commandes, 1,050 DH → 🎉 SILVER

Commande 4: 600 DH → Remise 5% (-30 DH) → Total: 570 DH
Commande 5: 3,500 DH → Remise 5% (-175 DH) → 5,325 DH cumulé → 🎉 GOLD
Commande 6: 900 DH → Remise 10% (-90 DH) → Total: 810 DH
```

---

## 💰 Système de Paiements Multi-Moyens

### Moyens de Paiement Acceptés

| Type | Caractéristiques | Informations requises |
|------|------------------|----------------------|
| **ESPECES** | Limite: 20,000 DH max par paiement (loi marocaine)<br>Paiement immédiat | Référence reçu |
| **CHÈQUE** | Peut être différé<br>Statuts: EN_ATTENTE → ENCAISSÉ/REJETÉ | Numéro, banque, échéance |
| **VIREMENT** | Immédiat ou différé | Référence, banque |

### Règle Importante

> ⚠️ Une commande doit être **totalement payée** (montantRestant = 0) avant validation ADMIN → CONFIRMED

### Exemple de Paiement Fractionné

**Commande: 10,000 DH**

| Date | Montant | Moyen | Détails | Restant | Statut Commande |
|------|---------|-------|---------|---------|-----------------|
| 05/11 | 6,000 DH | ESPECES | REÇU-001 | 4,000 DH | PENDING |
| 08/11 | 3,000 DH | CHÈQUE | CHQ-7894561, BMCE, Échéance 20/11 | 1,000 DH | PENDING |
| 12/11 | 1,000 DH | VIREMENT | VIR-2025-11-12-4521, Attijariwafa | 0 DH | ✅ Peut être CONFIRMED |

---

## 📐 Calcul des Montants

### Formules de Calcul

```
1. Sous-total HT = Σ (Prix HT × Quantité) pour chaque produit

2. Remise Fidélité = Sous-total HT × Taux remise (si seuil atteint)

3. Remise Promo = (Sous-total HT - Remise Fidélité) × 5% (si code valide)

4. Montant Total Remise = Remise Fidélité + Remise Promo

5. Montant HT après Remise = Sous-total HT - Montant Total Remise

6. TVA (20%) = Montant HT après Remise × 0.20

7. Total TTC = Montant HT après Remise + TVA
```

### Exemple de Calcul

```
Sous-total: 1,000 DH
Remise fidélité (10%): -100 DH
─────────────────────────────
Montant HT: 900 DH
TVA (20%): 180 DH
─────────────────────────────
Total TTC: 1,080 DH
```

---

## 🔄 Gestion des Statuts de Commande

### Diagramme de Transition

```
       ┌──────────┐
       │ PENDING  │ (Commande créée)
       └────┬─────┘
            │
     ┌──────┼──────┐
     │      │      │
     ▼      ▼      ▼
┌─────────┐ ┌──────────┐ ┌──────────┐
│REJECTED │ │CONFIRMED │ │CANCELED  │
│(Stock)  │ │(Validée) │ │(Annulée) │
└─────────┘ └──────────┘ └──────────┘
```

### Transitions

| De | Vers | Condition | Acteur |
|----|------|-----------|--------|
| PENDING | REJECTED | Stock insuffisant | Système |
| PENDING | CONFIRMED | Paiement complet | ADMIN |
| PENDING | CANCELED | Annulation manuelle | ADMIN |

---

## 🎨 Enums du Système

### UserRole
```java
ADMIN    // Employé MicroTech
CLIENT   // Entreprise cliente
```

### CustomerTier
```java
BASIC     // Niveau par défaut
SILVER    // 3 commandes OU 1,000 DH
GOLD      // 10 commandes OU 5,000 DH
PLATINUM  // 20 commandes OU 15,000 DH
```

### OrderStatus
```java
PENDING    // En attente
CONFIRMED  // Validée
CANCELED   // Annulée
REJECTED   // Refusée
```

### PaymentStatus
```java
EN_ATTENTE  // Non encaissé
ENCAISSÉ    // Reçu
REJETÉ      // Rejeté
```

### PaymentType
```java
ESPECES
CHÈQUE
VIREMENT
```

---

## 🚨 Gestion des Erreurs

### Codes HTTP

| Code | Signification | Exemple |
|------|---------------|---------|
| 400 | Bad Request | Données invalides |
| 401 | Unauthorized | Non authentifié |
| 403 | Forbidden | Permissions insuffisantes |
| 404 | Not Found | Ressource inexistante |
| 422 | Unprocessable Entity | Règle métier violée (stock, paiement) |
| 500 | Internal Server Error | Erreur serveur |

### Format de Réponse d'Erreur

```json
{
  "timestamp": "2025-11-24T10:30:00",
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Stock insuffisant pour le produit 'Clavier Gaming'",
  "path": "/api/orders"
}
```

---

## 📝 Règles Métier Critiques

### Validations
- ✅ Quantité demandée ≤ Stock disponible
- ✅ Tous les montants arrondis à 2 décimales
- ✅ Code promo format: `PROMO-XXXX` (usage unique possible)
- ✅ TVA: 20% par défaut (paramétrable)
- ✅ Paiement ESPECES ≤ 20,000 DH

### Contraintes Métier
- ❌ Commande sans client = Refusée
- ❌ Commande sans article = Refusée
- ❌ Validation CONFIRMED impossible si paiement incomplet
- ❌ Modification impossible sur commande avec statut final

---

## 🚀 Installation et Configuration

### Prérequis

```bash
Java 8+
Maven 3.6+
PostgreSQL 12+ ou MySQL 8+
Postman ou Swagger
```

### Étapes d'Installation

1. **Cloner le projet**
```bash
git clone https://github.com/votre-username/smartshop.git
cd smartshop
```

2. **Configurer la base de données**
```properties
# application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/smartshop
spring.datasource.username=votre_username
spring.datasource.password=votre_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

3. **Installer les dépendances**
```bash
mvn clean install
```

4. **Lancer l'application**
```bash
mvn spring-boot:run
```

5. **Accéder à Swagger** (optionnel)
```
http://localhost:8080/swagger-ui.html
```

---

## 🧪 Tests

### Exécuter les Tests Unitaires
```bash
mvn test
```

### Couverture de Tests
- Tests unitaires avec **JUnit 5**
- Mocking avec **Mockito**
- Tests des services et repositories
- Tests des validations métier

---

## 📡 Endpoints API Principaux

### Authentification
```
POST   /api/auth/login
POST   /api/auth/logout
GET    /api/auth/current-user
```

### Clients
```
POST   /api/clients              [ADMIN]
GET    /api/clients              [ADMIN]
GET    /api/clients/{id}         [ADMIN, CLIENT(own)]
PUT    /api/clients/{id}         [ADMIN]
GET    /api/clients/{id}/orders  [ADMIN, CLIENT(own)]
GET    /api/clients/{id}/stats   [ADMIN, CLIENT(own)]
```

### Produits
```
POST   /api/products          [ADMIN]
GET    /api/products          [ALL]
GET    /api/products/{id}     [ALL]
PUT    /api/products/{id}     [ADMIN]
DELETE /api/products/{id}     [ADMIN]
```

### Commandes
```
POST   /api/orders                    [ADMIN]
GET    /api/orders/{id}               [ADMIN, CLIENT(own)]
PUT    /api/orders/{id}/confirm       [ADMIN]
PUT    /api/orders/{id}/cancel        [ADMIN]
POST   /api/orders/{id}/payments      [ADMIN]
```

### Paiements
```
POST   /api/payments                  [ADMIN]
GET    /api/payments/{id}             [ADMIN]
PUT    /api/payments/{id}/encaisser   [ADMIN]
PUT    /api/payments/{id}/rejeter     [ADMIN]
```

---

## 📂 Structure du Projet

```
src/main/java/com/microtech/smartshop/
│
├── controller/          # Endpoints REST
│   ├── AuthController
│   ├── ClientController
│   ├── ProductController
│   ├── OrderController
│   └── PaymentController
│
├── service/            # Logique métier
│   ├── impl/
│   ├── AuthService
│   ├── ClientService
│   ├── ProductService
│   ├── OrderService
│   └── PaymentService
│
├── repository/         # Accès données
│   ├── UserRepository
│   ├── ClientRepository
│   ├── ProductRepository
│   ├── OrderRepository
│   └── PaymentRepository
│
├── entity/            # Entités JPA
│   ├── User
│   ├── Client
│   ├── Product
│   ├── Order
│   ├── OrderItem
│   └── Payment
│
├── dto/               # Data Transfer Objects
│   ├── request/
│   └── response/
│
├── mapper/            # MapStruct Mappers
│   ├── ClientMapper
│   ├── ProductMapper
│   ├── OrderMapper
│   └── PaymentMapper
│
├── enums/             # Énumérations
│   ├── UserRole
│   ├── CustomerTier
│   ├── OrderStatus
│   ├── PaymentStatus
│   └── PaymentType
│
├── exception/         # Exceptions métier
│   ├── GlobalExceptionHandler
│   ├── ResourceNotFoundException
│   ├── BusinessRuleException
│   └── InsufficientStockException
│
├── config/            # Configuration
│   ├── AppConfig
│   └── SwaggerConfig
│
└── util/              # Utilitaires
    └── CalculationUtil
```

---

## 📊 Diagramme de Classes UML

> Voir le fichier `uml-class-diagram.png` dans le dépôt

---

## 📋 Suivi de Projet

### JIRA Board
- **To Do** : Tâches planifiées
- **In Progress** : En cours de développement
- **Testing** : En phase de test
- **Done** : Complété et validé

### Sprint Planning
- Sprint 1 : Setup + Authentification + Entités
- Sprint 2 : Clients + Produits + CRUD
- Sprint 3 : Commandes + Système fidélité
- Sprint 4 : Paiements multi-moyens
- Sprint 5 : Tests + Documentation

---

## 🎯 Critères de Réussite

✅ L'application démarre sans erreur  
✅ Connexion DB fonctionnelle  
✅ Validations métier correctes (stock, remises, TVA)  
✅ Gestion erreurs cohérente (codes HTTP + JSON)  
✅ Architecture claire (Controller-Service-Repository-DTO)  
✅ Comportement conforme aux règles de gestion  
✅ Tests unitaires couvrent les cas critiques  
✅ Documentation API complète (Swagger/Postman)  

---

## 👥 Contributeurs

**Développeur** : charaf eddine
**Client** : MicroTech Maroc  

---

## 📅 Calendrier

| Phase | Date |
|-------|------|
| Lancement | 24/11/2025 |


---

## 📞 Support

Pour toute question ou problème :
- 📧 Email: charafeddinetbibzat]@gmail.com
- 📱 Téléphone: +212 651928482

---

## 📜 Licence

Ce projet est développé dans un cadre pédagogique pour **MicroTech Maroc**.

---

**Développé avec ❤️ par l'équipe MicroTech**
