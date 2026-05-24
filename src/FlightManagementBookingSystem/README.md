# ✈️ Flight Management Booking System

A full-stack flight management and booking web application with stunning 3D animations, built with Java, SQLite, and modern web technologies.

![Java](https://img.shields.io/badge/Java-17-orange)
![SQLite](https://img.shields.io/badge/SQLite-3.36-blue)
![Three.js](https://img.shields.io/badge/Three.js-3D-green)
![Tailwind CSS](https://img.shields.io/badge/Tailwind-CSS-38bdf8)

## 🌟 Features

- **Beautiful 3D Animated UI** - Three.js particle system with interactive backgrounds
- **Modern Design** - Tailwind CSS with glass-morphism effects and smooth animations
- **RESTful API** - Java HTTP Server with JSON responses
- **SQLite Database** - Persistent data storage for flights, passengers, and bookings
- **Real-time Updates** - Live statistics and automatic refresh
- **Responsive Design** - Works on desktop, tablet, and mobile devices

## 🚀 Quick Start (Local Development)

### Prerequisites
- Java 17 or higher
- PowerShell (Windows) or Terminal (macOS/Linux)

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/DailyP549/flight-management-booking-system.git
   cd flight-management-booking-system
   ```

2. **Download Dependencies**
   ```bash
   # Create lib directory
   mkdir lib
   
   # Download SQLite JDBC Driver
   curl -L -o lib/sqlite-jdbc-3.36.0.3.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.36.0.3/sqlite-jdbc-3.36.0.3.jar
   
   # Download JSON Library
   curl -L -o lib/json-20231013.jar https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar
   ```

3. **Compile the Application**
   ```bash
   javac -cp "lib/*" -d out src/FlightManagementBookingSystem/*.java
   ```

4. **Run the Server**
   ```bash
   java -cp "out:lib/*" FlightManagementBookingSystem.FlightServer
   ```

5. **Open in Browser**
   Navigate to: [http://localhost:8080](http://localhost:8080)

## 🌐 Deploy to Render.com (Free Hosting)

### Option 1: One-Click Deploy

[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/DailyP549/flight-management-booking-system)

### Option 2: Manual Deployment

1. **Create a Render Account**
   - Go to [render.com](https://render.com)
   - Sign up with GitHub

2. **Create New Web Service**
   - Click "New +" → "Web Service"
   - Connect your GitHub repository
   - Select `flight-management-booking-system`

3. **Configure Service**
   - **Name**: `flight-management-booking-system`
   - **Environment**: `Java`
   - **Build Command**: 
     ```bash
     mkdir -p lib
     curl -L -o lib/sqlite-jdbc-3.36.0.3.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.36.0.3/sqlite-jdbc-3.36.0.3.jar
     curl -L -o lib/json-20231013.jar https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar
     javac -cp "lib/*" -d out src/FlightManagementBookingSystem/*.java
     ```
   - **Start Command**: 
     ```bash
     java -cp "out:lib/*" FlightManagementBookingSystem.FlightServer
     ```
   - **Plan**: Free

4. **Deploy**
   - Click "Create Web Service"
   - Wait for build to complete (~2-3 minutes)
   - Your app will be live at: `https://flight-management-booking-system.onrender.com`

## 📁 Project Structure

```
flight-management-booking-system/
├── src/
│   └── FlightManagementBookingSystem/
│       ├── FlightServer.java          # HTTP Server & REST API
│       ├── Flight.java                # Flight model
│       ├── Booking.java               # Booking model
│       ├── Passenger.java             # Passenger model
│       ├── Main.java                  # CLI version
│       ├── index.html                 # Web frontend
│       ├── app.js                     # Frontend JavaScript
│       ├── styles.css                 # Custom CSS
│       └── three-background.js        # Three.js 3D animation
├── lib/                               # Dependencies (created during build)
│   ├── sqlite-jdbc-3.36.0.3.jar
│   └── json-20231013.jar
├── out/                               # Compiled classes (created during build)
├── render.yaml                        # Render.com configuration
├── Procfile                           # Heroku/Railway configuration
├── system.properties                  # Java version config
└── README.md                          # This file
```

## 🔧 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/flights` | Get all flights |
| POST | `/api/flights` | Create new flight |
| GET | `/api/passengers` | Get all passengers |
| POST | `/api/passengers` | Create new passenger |
| GET | `/api/bookings` | Get all bookings |
| POST | `/api/bookings` | Create new booking |
| PUT | `/api/bookings/{id}` | Cancel booking |
| GET | `/api/stats` | Get system statistics |

## 🎨 Technologies Used

### Backend
- **Java 17** - Core programming language
- **Java HTTP Server** - Built-in HTTP server
- **SQLite** - Lightweight database
- **org.json** - JSON processing

### Frontend
- **Three.js** - 3D particle animations
- **Tailwind CSS** - Utility-first CSS framework
- **Vanilla JavaScript** - DOM manipulation and API calls
- **Fetch API** - HTTP requests

## 📸 Screenshots

The application features:
- Stunning 3D particle background with mouse interaction
- Glass-morphism UI with backdrop blur effects
- Smooth animations and transitions
- Real-time statistics dashboard
- Flight cards with hover effects
- Responsive booking forms

## 🐛 Troubleshooting

### Port Already in Use
```bash
# Windows PowerShell
taskkill /F /IM java.exe

# Linux/macOS
lsof -ti:8080 | xargs kill -9
```

### Database Not Found
The SQLite database (`flight_booking.db`) is created automatically on first run.

### Dependencies Missing
Ensure both JAR files are in the `lib/` directory before compiling.

## 📝 License

MIT License - feel free to use this project for learning or production.

## 👤 Author

**Prema**
- GitHub: [@DailyP549](https://github.com/DailyP549)

## 🙏 Acknowledgments

- Three.js for 3D graphics
- Tailwind CSS for utility classes
- SQLite for lightweight database
- Render.com for free hosting

---

**Enjoy booking your flights! ✈️🌍**
