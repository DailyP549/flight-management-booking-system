// Three.js 3D Animated Background
const scene = new THREE.Scene();
const camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
const renderer = new THREE.WebGLRenderer({ alpha: true, antialias: true });
renderer.setSize(window.innerWidth, window.innerHeight);
renderer.setPixelRatio(window.devicePixelRatio);
document.getElementById('canvas-container').appendChild(renderer.domElement);

// Create particles
const particlesGeometry = new THREE.BufferGeometry();
const particlesCount = 2000;
const posArray = new Float32Array(particlesCount * 3);
const colorsArray = new Float32Array(particlesCount * 3);

for(let i = 0; i < particlesCount * 3; i += 3) {
    // Positions
    posArray[i] = (Math.random() - 0.5) * 50;
    posArray[i+1] = (Math.random() - 0.5) * 50;
    posArray[i+2] = (Math.random() - 0.5) * 50;
    
    // Colors (purple, blue, pink gradient)
    colorsArray[i] = 0.4 + Math.random() * 0.2;     // R
    colorsArray[i+1] = 0.3 + Math.random() * 0.3;   // G
    colorsArray[i+2] = 0.8 + Math.random() * 0.2;   // B
}

particlesGeometry.setAttribute('position', new THREE.BufferAttribute(posArray, 3));
particlesGeometry.setAttribute('color', new THREE.BufferAttribute(colorsArray, 3));

const particlesMaterial = new THREE.PointsMaterial({
    size: 0.1,
    vertexColors: true,
    transparent: true,
    opacity: 0.8,
    blending: THREE.AdditiveBlending
});

const particlesMesh = new THREE.Points(particlesGeometry, particlesMaterial);
scene.add(particlesMesh);

// Add floating geometric shapes
const shapes = [];
const geometries = [
    new THREE.IcosahedronGeometry(1, 0),
    new THREE.OctahedronGeometry(1, 0),
    new THREE.TetrahedronGeometry(1, 0)
];

for(let i = 0; i < 15; i++) {
    const geometry = geometries[Math.floor(Math.random() * geometries.length)];
    const material = new THREE.MeshPhongMaterial({
        color: new THREE.Color().setHSL(0.6 + Math.random() * 0.3, 0.7, 0.5),
        transparent: true,
        opacity: 0.3,
        wireframe: true
    });
    const mesh = new THREE.Mesh(geometry, material);
    mesh.position.set(
        (Math.random() - 0.5) * 30,
        (Math.random() - 0.5) * 30,
        (Math.random() - 0.5) * 30
    );
    mesh.rotation.set(
        Math.random() * Math.PI,
        Math.random() * Math.PI,
        Math.random() * Math.PI
    );
    const scale = Math.random() * 2 + 0.5;
    mesh.scale.set(scale, scale, scale);
    mesh.userData = {
        rotationSpeed: {
            x: (Math.random() - 0.5) * 0.01,
            y: (Math.random() - 0.5) * 0.01,
            z: (Math.random() - 0.5) * 0.01
        },
        floatSpeed: Math.random() * 0.005 + 0.002,
        floatOffset: Math.random() * Math.PI * 2
    };
    shapes.push(mesh);
    scene.add(mesh);
}

// Add lights
const ambientLight = new THREE.AmbientLight(0x404040, 2);
scene.add(ambientLight);

const pointLight1 = new THREE.PointLight(0x667eea, 2, 50);
pointLight1.position.set(10, 10, 10);
scene.add(pointLight1);

const pointLight2 = new THREE.PointLight(0x764ba2, 2, 50);
pointLight2.position.set(-10, -10, -10);
scene.add(pointLight2);

camera.position.z = 20;

// Mouse interaction
let mouseX = 0;
let mouseY = 0;

document.addEventListener('mousemove', (event) => {
    mouseX = (event.clientX / window.innerWidth) * 2 - 1;
    mouseY = -(event.clientY / window.innerHeight) * 2 + 1;
});

// Animation
const clock = new THREE.Clock();

function animate() {
    requestAnimationFrame(animate);
    const elapsedTime = clock.getElapsedTime();

    // Rotate particles
    particlesMesh.rotation.y = elapsedTime * 0.05;
    particlesMesh.rotation.x = elapsedTime * 0.03;

    // Animate shapes
    shapes.forEach((shape) => {
        shape.rotation.x += shape.userData.rotationSpeed.x;
        shape.rotation.y += shape.userData.rotationSpeed.y;
        shape.rotation.z += shape.userData.rotationSpeed.z;
        
        shape.position.y += Math.sin(elapsedTime * shape.userData.floatSpeed + shape.userData.floatOffset) * 0.02;
    });

    // Camera follows mouse
    camera.position.x += (mouseX * 2 - camera.position.x) * 0.05;
    camera.position.y += (mouseY * 2 - camera.position.y) * 0.05;
    camera.lookAt(scene.position);

    renderer.render(scene, camera);
}

animate();

// Handle window resize
window.addEventListener('resize', () => {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
});
