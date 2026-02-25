# MicroTech - E-commerce Microservices API

Une application microservices robuste pour la gestion d'une plateforme e-commerce, construite avec **Spring Boot 4.0** et **Java 17**.

## 📋 Aperçu du Projet

MicroTech est un système backend complet pour une plateforme e-commerce avec gestion des utilisateurs, produits, commandes, paiements et codes promotionnels.

## ✨ Fonctionnalités Principales

- **Authentification & Autorisation** - Système de sécurité avec rôles et permissions
- **Gestion des Clients** - Création et gestion des profils clients
- **Gestion des Produits** - Catalogue de produits avec détails
- **Gestion des Commandes** - Création et suivi des commandes clients
- **Système de Paiement** - Traitement des paiements
- **Codes Promotionnels** - Gestion des réductions et promotions
- **Dashboard Admin** - Statistiques et métriques d'administration
- **Sécurité** - Authentification JWT et contrôle d'accès basé sur les rôles

## 🛠️ Stack Technologique

- **Java 17**
- **Spring Boot 4.0**
- **Spring Data JPA**
- **PostgreSQL** (via Docker Compose)
- **Maven**
- **jBCrypt** (hachage des mots de passe)

## 📦 Architecture

```
src/main/java/org/example/microTech/
├── annotations/      # Annotations personnalisées
├── aspects/          # Aspects AOP pour la sécurité
├── config/           # Configuration Spring (CORS, etc.)
├── controllers/      # Contrôleurs REST API
├── dto/              # Objets de transfert de données
├── entities/         # Entités JPA
├── enums/            # Énumérations
├── exceptions/       # Exceptions personnalisées
├── mappers/          # Mappers DTO <-> Entity
├── repositories/     # Accès aux données
├── services/         # Logique métier
├── utils/            # Utilitaires
└── validation/       # Validations personnalisées
```

## 🚀 Démarrage Rapide

### Prérequis

- Java 17+
- Maven 3.6+
- Docker & Docker Compose (optionnel)

### Installation

1. **Cloner le projet**
   ```bash
   git clone <repository-url>
   cd Micro_tech
   ```

2. **Configurer les variables d'environnement**
   
   Créez un fichier `.env` à la racine du projet :
   ```env
   DB_URL=jdbc:postgresql://localhost:5432/microtech
   DB_USER=postgres
   DB_PASSWORD=your_password
   ```

3. **Démarrer PostgreSQL (optionnel avec Docker Compose)**
   ```bash
   docker-compose up -d
   ```

4. **Builder le projet**
   ```bash
   mvn clean build
   ```

5. **Lancer l'application**
   ```bash
   mvn spring-boot:run
   ```

L'application sera disponible à : `http://localhost:8080`

## 📚 Points de Terminaison Principaux

- `POST /api/auth/login` - Authentification
- `GET/POST /api/clients` - Gestion des clients
- `GET/POST /api/products` - Gestion des produits
- `GET/POST /api/orders` - Gestion des commandes
- `POST /api/payments` - Traitement des paiements
- `GET/POST /api/promo-codes` - Gestion des codes promo
- `GET /api/admin/dashboard` - Statistiques admin

## 🧪 Tests

```bash
mvn test
```

## 📁 Structure des Fichiers

- `pom.xml` - Dépendances Maven
- `compose.yaml` - Configuration Docker Compose
- `src/main/resources/application.properties` - Configuration de l'application

## 📝 Notes de Développement

- Utilisez les annotations `@Secured` pour protéger les endpoints
- Les mappers DTO assurent une séparation claire entre les données internes et API
- Les aspects AOP gèrent la sécurité et les validations
- Configuration CORS incluse pour les demandes cross-origin

## 📄 Licence

[À spécifier]

## 👥 Contributeurs

[À spécifier]
