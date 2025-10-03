# Stripe Payment Integration POC

A professional proof-of-concept demonstrating Stripe payment integration with Java Spring Boot backend and Angular frontend.

## 🚀 Quick Start

### Prerequisites
- Java 11+
- Maven 3.6+
- Node.js 16+
- Git

### Setup & Run

1. **Clone and Setup**
   ```bash
   git clone <repository-url>
   cd poc-stripe
   ```

2. **Backend Setup**
   ```bash
   cd backend
   mvn spring-boot:run
   ```

3. **Frontend Setup**
   ```bash
   cd frontend
   npm install
   npm start
   ```

4. **Access Application**
   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8080

## 🏗️ Architecture

```
┌─────────────────┐    HTTP/REST    ┌─────────────────┐
│   Angular UI    │ ◄──────────────► │  Spring Boot    │
│   (Frontend)    │                  │   (Backend)     │
└─────────────────┘                  └─────────────────┘
                                              │
                                              │ Stripe API
                                              ▼
                                     ┌─────────────────┐
                                     │   Stripe API    │
                                     │   (Payments)    │
                                     └─────────────────┘
```

## 💳 Pricing Tiers

| Users | Monthly Price | Stripe Price ID |
|-------|---------------|-----------------|
| 50    | $50.00       | price_starter    |
| 100   | $90.00       | price_growth     |
| 200   | $160.00      | price_professional|
| ~~300~~ | ~~$220.00~~ | ~~price_enterprise~~ *(available for future expansion)* |

## 🧪 Test Cards

Use these Stripe test cards for demo:

- **Success**: `4242424242424242`
- **Decline**: `4000000000000002`
- **Requires Authentication**: `4000002500003155`

## 📁 Project Structure

```
poc-stripe/
├── backend/                 # Java Spring Boot API
│   ├── src/main/java/
│   │   └── com/stripe/poc/
│   │       ├── StripeApplication.java
│   │       ├── controller/
│   │       ├── service/
│   │       ├── model/
│   │       └── config/
│   └── pom.xml
├── frontend/               # Angular Application
│   ├── src/app/
│   │   ├── components/
│   │   ├── services/
│   │   └── models/
│   └── package.json
└── docs/                  # Documentation
```

## 🔧 Configuration

### Environment Variables

Create `.env` files in both backend and frontend:

**Backend (.env)**
```
STRIPE_SECRET_KEY=sk_test_...
STRIPE_PUBLISHABLE_KEY=pk_test_...
```

**Frontend (.env)**
```
STRIPE_PUBLISHABLE_KEY=pk_test_...
API_BASE_URL=http://localhost:8080
```

## 🎯 Demo Flow

1. **Product Selection** - Choose user tier (50/100/200 users)
2. **Stripe Checkout** - Secure hosted payment page
3. **Payment Processing** - Stripe handles payment processing
4. **Confirmation** - Success/failure redirects

## 🚀 Production Deployment

### Backend
```bash
mvn clean package
java -jar target/stripe-poc-backend-1.0.0.jar
```

### Frontend
```bash
ng build --prod
# Deploy dist/ folder to web server
```

## 📊 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get available products |
| POST | `/api/create-checkout-session` | Create Stripe checkout session |
| GET | `/api/checkout-session/{id}` | Get checkout session details |

## 🔒 Security Features

- Environment-based configuration
- CORS protection
- Input validation
- Secure payment form (PCI compliant)

## 📝 Development Notes

- Uses Stripe test mode for development
- Includes comprehensive error handling
- Responsive design with Angular Material
- Professional UI/UX patterns
- Git-friendly project structure

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License.
