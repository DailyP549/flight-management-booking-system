const API_BASE = 'http://localhost:8080/api';

// Tab Navigation
function showTab(tabId) {
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.add('hidden');
        tab.classList.remove('animate-fade-in');
    });
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('bg-gradient-to-r', 'from-blue-500', 'to-purple-500', 'text-white', 'shadow-lg');
        btn.classList.add('hover:bg-white/20', 'text-gray-200');
    });
    
    document.getElementById(tabId).classList.remove('hidden');
    document.getElementById(tabId).classList.add('animate-fade-in');
    event.target.classList.remove('hover:bg-white/20', 'text-gray-200');
    event.target.classList.add('bg-gradient-to-r', 'from-blue-500', 'to-purple-500', 'text-white', 'shadow-lg');

    if (tabId === 'availableFlights') loadAvailableFlights();
    if (tabId === 'viewPassengers') loadPassengers();
    if (tabId === 'viewBookings') loadBookings();
    if (tabId === 'createBooking') populateBookingDropdowns();
}

// Show message
function showMessage(elementId, message, type) {
    const msgElement = document.getElementById(elementId);
    msgElement.textContent = message;
    msgElement.className = `message px-6 py-4 rounded-xl mt-4 font-semibold animate-slide-up ${
        type === 'success' ? 'bg-gradient-to-r from-green-500/20 to-emerald-500/20 text-green-300 border-l-4 border-green-400' :
        type === 'error' ? 'bg-gradient-to-r from-red-500/20 to-pink-500/20 text-red-300 border-l-4 border-red-400' :
        'bg-gradient-to-r from-yellow-500/20 to-orange-500/20 text-yellow-300 border-l-4 border-yellow-400'
    }`;
    msgElement.classList.remove('hidden');
    setTimeout(() => {
        msgElement.classList.add('hidden');
    }, 5000);
}

// Update statistics with animation
async function updateStats() {
    try {
        const response = await fetch(`${API_BASE}/stats`);
        const stats = await response.json();
        
        animateNumber('totalFlights', stats.totalFlights);
        animateNumber('totalPassengers', stats.totalPassengers);
        animateNumber('totalBookings', stats.totalBookings);
        animateNumber('totalSeats', stats.totalSeats);
    } catch (error) {
        console.error('Error loading stats:', error);
    }
}

function animateNumber(elementId, targetNumber) {
    const element = document.getElementById(elementId);
    const currentNumber = parseInt(element.textContent) || 0;
    const increment = (targetNumber - currentNumber) / 20;
    let step = 0;
    
    const timer = setInterval(() => {
        step++;
        const value = Math.round(currentNumber + (increment * step));
        element.textContent = value;
        
        if (step >= 20) {
            element.textContent = targetNumber;
            clearInterval(timer);
        }
    }, 30);
}

// Load available flights
async function loadAvailableFlights() {
    const container = document.getElementById('availableFlightsList');
    container.innerHTML = '<div class="flex justify-center py-16"><div class="loading-spinner"></div></div>';
    
    try {
        const response = await fetch(`${API_BASE}/flights`);
        const flights = await response.json();
        const availableFlights = flights.filter(f => f.availableSeats > 0);
        
        if (availableFlights.length === 0) {
            container.innerHTML = `
                <div class="glass rounded-2xl p-16 text-center animate-fade-in">
                    <div class="text-8xl mb-6 animate-float">🛫</div>
                    <h3 class="text-3xl font-bold mb-4 bg-gradient-to-r from-blue-400 to-purple-400 bg-clip-text text-transparent">No Flights Available</h3>
                    <p class="text-gray-300 mb-8 text-lg">Add a flight to get started on your journey!</p>
                    <button onclick="showTab('addFlight')" class="btn-glow bg-gradient-to-r from-blue-500 to-purple-500 px-8 py-4 rounded-xl font-semibold shadow-lg hover:shadow-xl transition-all">
                        ➕ Add Your First Flight
                    </button>
                </div>
            `;
            return;
        }
        
        container.innerHTML = availableFlights.map((flight, index) => `
            <div class="glass rounded-2xl p-6 card-hover animate-slide-up" style="animation-delay: ${index * 0.1}s">
                <div class="flex flex-col lg:flex-row lg:items-center justify-between gap-6">
                    <div class="flex-1">
                        <div class="flex items-center gap-4 mb-4">
                            <span class="text-3xl">✈️</span>
                            <h3 class="text-2xl font-bold text-blue-400">${flight.source}</h3>
                            <span class="text-2xl text-gray-400">→</span>
                            <h3 class="text-2xl font-bold text-purple-400">${flight.destination}</h3>
                        </div>
                        <div class="grid md:grid-cols-3 gap-4 text-sm">
                            <div class="bg-white/5 rounded-lg p-3 backdrop-blur-sm">
                                <p class="text-gray-400 mb-1">🛫 Departure</p>
                                <p class="font-semibold">${formatDate(flight.departureTime)}</p>
                            </div>
                            <div class="bg-white/5 rounded-lg p-3 backdrop-blur-sm">
                                <p class="text-gray-400 mb-1">🛬 Arrival</p>
                                <p class="font-semibold">${formatDate(flight.arrivalTime)}</p>
                            </div>
                            <div class="bg-white/5 rounded-lg p-3 backdrop-blur-sm">
                                <p class="text-gray-400 mb-1">💺 Available</p>
                                <p class="font-semibold text-green-400">${flight.availableSeats} seats</p>
                            </div>
                        </div>
                    </div>
                    <div class="lg:text-right space-y-4">
                        <div>
                            <p class="text-gray-400 text-sm mb-1">Price per seat</p>
                            <p class="text-4xl font-bold bg-gradient-to-r from-green-400 to-emerald-400 bg-clip-text text-transparent">$${flight.price.toFixed(2)}</p>
                        </div>
                        <button onclick="quickBook('${flight.id}')" class="btn-glow bg-gradient-to-r from-pink-500 to-purple-500 px-6 py-3 rounded-xl font-semibold shadow-lg hover:shadow-xl transition-all w-full">
                            🎫 Book Now
                        </button>
                    </div>
                </div>
            </div>
        `).join('');
    } catch (error) {
        container.innerHTML = `
            <div class="glass rounded-2xl p-16 text-center animate-fade-in bg-red-500/10">
                <div class="text-6xl mb-6">❌</div>
                <h3 class="text-2xl font-bold text-red-400 mb-4">Failed to Load Flights</h3>
                <p class="text-gray-300 mb-6">${error.message}</p>
                <button onclick="loadAvailableFlights()" class="btn-glow bg-gradient-to-r from-red-500 to-pink-500 px-6 py-3 rounded-xl font-semibold shadow-lg">
                    🔄 Retry
                </button>
            </div>
        `;
    }
}

// Load passengers
async function loadPassengers() {
    const tbody = document.getElementById('passengersBody');
    tbody.innerHTML = '<tr><td colspan="4" class="text-center py-8"><div class="loading-spinner mx-auto"></div></td></tr>';
    
    try {
        const response = await fetch(`${API_BASE}/passengers`);
        const passengers = await response.json();
        
        if (passengers.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="4" class="text-center py-16">
                        <div class="text-6xl mb-4">👥</div>
                        <p class="text-gray-400">No passengers registered yet</p>
                    </td>
                </tr>
            `;
            return;
        }
        
        tbody.innerHTML = passengers.map((p, index) => `
            <tr class="border-t border-white/10 hover:bg-white/5 transition-all animate-fade-in" style="animation-delay: ${index * 0.05}s">
                <td class="px-6 py-4 font-mono text-sm">${p.id}</td>
                <td class="px-6 py-4 font-semibold">${p.name}</td>
                <td class="px-6 py-4">${p.email}</td>
                <td class="px-6 py-4">${p.phone}</td>
            </tr>
        `).join('');
    } catch (error) {
        tbody.innerHTML = `<tr><td colspan="4" class="text-center py-8 text-red-400">❌ Error: ${error.message}</td></tr>`;
    }
}

// Load bookings
async function loadBookings() {
    const tbody = document.getElementById('bookingsBody');
    tbody.innerHTML = '<tr><td colspan="8" class="text-center py-8"><div class="loading-spinner mx-auto"></div></td></tr>';
    
    try {
        const response = await fetch(`${API_BASE}/bookings`);
        const bookings = await response.json();
        
        if (bookings.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="8" class="text-center py-16">
                        <div class="text-6xl mb-4">🎫</div>
                        <p class="text-gray-400">No bookings created yet</p>
                    </td>
                </tr>
            `;
            return;
        }
        
        tbody.innerHTML = bookings.map((b, index) => `
            <tr class="border-t border-white/10 hover:bg-white/5 transition-all animate-fade-in" style="animation-delay: ${index * 0.05}s">
                <td class="px-6 py-4 font-mono text-sm">${b.id}</td>
                <td class="px-6 py-4">${b.flightId}</td>
                <td class="px-6 py-4">${b.passengerId}</td>
                <td class="px-6 py-4 text-center">${b.seats}</td>
                <td class="px-6 py-4 font-semibold text-green-400">$${b.amount.toFixed(2)}</td>
                <td class="px-6 py-4 text-sm">${formatDate(b.bookingTime)}</td>
                <td class="px-6 py-4">
                    <span class="px-3 py-1 rounded-full text-xs font-semibold ${
                        b.status === 'CONFIRMED' ? 'bg-green-500/20 text-green-300' : 'bg-red-500/20 text-red-300'
                    }">
                        ${b.status}
                    </span>
                </td>
                <td class="px-6 py-4">
                    ${b.status === 'CONFIRMED' ? 
                        `<button onclick="cancelBooking('${b.id}')" class="btn-glow bg-red-500 hover:bg-red-600 px-4 py-2 rounded-lg text-sm font-semibold transition-all">
                            Cancel
                        </button>` : 
                        '<span class="text-gray-500">-</span>'
                    }
                </td>
            </tr>
        `).join('');
    } catch (error) {
        tbody.innerHTML = `<tr><td colspan="8" class="text-center py-8 text-red-400">❌ Error: ${error.message}</td></tr>`;
    }
}

// Populate booking dropdowns
async function populateBookingDropdowns() {
    const flightSelect = document.getElementById('bookingFlightSelect');
    const passengerSelect = document.getElementById('bookingPassengerSelect');
    
    flightSelect.innerHTML = '<option value="">-- Choose a Flight --</option>';
    passengerSelect.innerHTML = '<option value="">-- Choose a Passenger --</option>';
    
    try {
        const [flightsRes, passengersRes] = await Promise.all([
            fetch(`${API_BASE}/flights`),
            fetch(`${API_BASE}/passengers`)
        ]);
        
        const flights = await flightsRes.json();
        const passengers = await passengersRes.json();
        
        flights.filter(f => f.availableSeats > 0).forEach(f => {
            const option = document.createElement('option');
            option.value = f.id;
            option.textContent = `${f.source} → ${f.destination} (${f.availableSeats} seats)`;
            flightSelect.appendChild(option);
        });
        
        passengers.forEach(p => {
            const option = document.createElement('option');
            option.value = p.id;
            option.textContent = `${p.name} (${p.email})`;
            passengerSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error populating dropdowns:', error);
    }
}

// Quick book function
function quickBook(flightId) {
    showTab('createBooking');
    setTimeout(() => {
        document.getElementById('bookingFlightSelect').value = flightId;
        document.getElementById('bookingFlightSelect').dispatchEvent(new Event('change'));
    }, 100);
}

// Cancel booking
async function cancelBooking(bookingId) {
    if (!confirm('Are you sure you want to cancel this booking?')) return;
    
    try {
        const response = await fetch(`${API_BASE}/bookings/${bookingId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' }
        });
        
        const result = await response.json();
        alert('✅ ' + result.message);
        updateStats();
        loadBookings();
    } catch (error) {
        alert('❌ Error canceling booking: ' + error.message);
    }
}

// Format date
function formatDate(dateStr) {
    const date = new Date(dateStr);
    return date.toLocaleString('en-US', {
        month: 'short',
        day: 'numeric',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// Initialize
window.addEventListener('load', () => {
    updateStats();
    loadAvailableFlights();
});

// Auto-refresh stats every 10 seconds
setInterval(updateStats, 10000);
