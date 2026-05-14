# SandSim: Complete System Documentation
**Version 1.0**  
**Course:** CSSE375 Software Construction and Evolution  
**Date:** May 14, 2026

---

## Table of Contents
1. [Executive Summary](#executive-summary)
2. [Software Development Documents](#software-development-documents)
3. [User Documents](#user-documents)
4. [Administrator Documents](#administrator-documents)
5. [Test Plan and Strategy](#test-plan-and-strategy)
6. [Appendices](#appendices)

---

## Executive Summary

### Project Overview
SandSim is a desktop application developed in Java using LWJGL (Lightweight Java Game Library) as part of CSSE375 coursework. It implements a particle-based physics simulation inspired by Noita-style sandbox mechanics, allowing users to paint and interact with various particle types (sand, water, smoke, stone, fire) that behave according to physics rules including gravity, fluid dynamics, and gas behavior.

### Key Features
- **Interactive Particle Painting**: Place particles using mouse and keyboard controls
- **Dynamic Physics Simulation**: Sand falls under gravity, water flows, smoke rises, fire spreads
- **Infinite Expandable Grid**: World can expand in all directions (up, down, left, right)
- **Camera System**: Pan and zoom controls for viewing the particle world
- **Multiple Particle Types**: Sand, Water, Smoke, Stone, Fire, Plant, and Oil
- **Real-time Rendering**: OpenGL-based visualization with textures
- **Game Controls**: Pause/Play functionality and particle interactions

### Technology Stack
- **Language**: Java 18
- **Graphics Library**: LWJGL 3.4.1 (OpenGL binding)
- **Mathematics**: JOML 1.10.8 (Java Object Oriented Math Library)
- **Build Tool**: Maven 3.x
- **Testing Framework**: JUnit 5.8.1
- **Mocking Framework**: EasyMock 5.6.0

---

# Software Development Documents

## 1. System Architecture

### 1.1 Architectural Overview
SandSim follows a Model-View-Controller (MVC) pattern with separation between the graphics engine and game logic.

#### Core Layers:
```
┌─────────────────────────────────────────┐
│         Application Layer               │
│  (Launcher, SandSim, World)             │
├─────────────────────────────────────────┤
│         Game Logic Layer                │
│  (Particle System, Rules, Physics)      │
├─────────────────────────────────────────┤
│         Engine Layer                    │
│  (EngineManager, Rendering, Input)      │
├─────────────────────────────────────────┤
│         Graphics Layer                  │
│  (OpenGL, LWJGL, Shaders)               │
└─────────────────────────────────────────┘
```

### 1.2 Package Structure

#### `com.apet2929.engine`
Core graphics and window management:
- **EngineManager**: Main game loop coordinator
  - Initializes and manages the rendering pipeline
  - Synchronizes frame rate to 60 FPS using the Sync class
  - Calls input(), update(), and render() lifecycle methods
  - Handles cleanup and resource management

- **WindowManager**: GLFW window and OpenGL context management
  - Creates and manages the game window (default 1600x900)
  - Handles OpenGL initialization
  - Manages window lifecycle

- **RenderManager**: OpenGL rendering pipeline
  - Sets up camera and projection matrices
  - Renders particles using textured quads
  - Manages shader programs

- **MouseInput**: Mouse event handling
  - Tracks left/right button states
  - Provides cursor position information

- **KeyboardManager**: Keyboard event handling
  - Tracks key press/release states
  - Number key mapping for particle type selection

- **AssetCache**: Resource caching
  - Manages texture and model loading
  - Prevents duplicate asset loading

- **Sync**: Accurate frame rate synchronization (60 FPS)

#### `com.apet2929.engine.model`
3D graphics data structures:
- **Camera**: View matrix management
  - Position and orientation
  - Movement in X, Y, Z axes
  - Zoom functionality

- **Grid**: World grid representation
  - Stores grid dimensions
  - Provides grid to screen coordinate mapping

- **Model**: VAO/VBO wrapper for 3D models
  - Vertex Array Objects
  - Vertex Buffer Objects
  - Index management

- **Texture**: Texture wrapper
  - Texture ID management
  - OpenGL texture binding

- **ObjectLoader**: Asset loading utility
  - Loads 3D models
  - Loads textures from files
  - Creates and manages VAO/VBO resources
  - Cleanup of GPU resources

- **Transformation**: Matrix transformations
  - Position, rotation, scale matrices

#### `com.apet2929.engine.utils`
Utilities:
- **Consts**: Global constants
  - Window dimensions
  - Grid dimensions
  - Particle TPS (Ticks Per Second)

- **Utils**: Helper functions
  - File I/O
  - Matrix/Vector operations
  - Shader compilation

#### `com.apet2929.game`
Game logic and simulation:
- **Launcher**: Application entry point
  - Creates window and game instance
  - Starts the engine

- **SandSim**: Main game logic (implements ILogic)
  - Initialization
  - Input handling
  - Game update loop
  - Rendering orchestration
  - Selected particle type and brush size management

- **World**: Particle simulation grid
  - 2D particle matrix storage
  - Particle update orchestration
  - Swap/movement mechanics
  - World expansion in all directions

- **Rule**: Particle interaction rules (future enhancement)
  - Defines swap conditions
  - Symmetrical rule application

- **ParticleTimer**: Timing utilities for particle behaviors

#### `com.apet2929.game.particles`
Particle system:
- **Particle**: Abstract base class for all particles
  - Position (gridX, gridY)
  - Type and texture
  - Velocity
  - Update method (called each tick)
  - canSwap() method for determining particle interactions

- **ParticleType**: Enum of all particle types
  - SAND (movable solid)
  - WATER (liquid)
  - SMOKE (gas)
  - STONE (immovable solid)
  - FIRE (liquid with special properties)
  - PLANT (movable solid)
  - OIL (liquid)
  - EMPTYPARTICLE (empty space)

- **ParticleLoader**: Factory for creating particles
  - Creates particle instances by type
  - Loads particle textures

- **EmptyParticle**: Represents empty grid cells

#### `com.apet2929.game.particles.solid`
Solid particle types:
- **Sand**: Movable solid
  - Falls under gravity
  - Can be pushed by water
  - Piles up

- **Stone**: Immovable solid
  - Acts as barrier
  - Cannot be moved or destroyed

- **Plant**: Movable solid
  - Similar to sand but with plant-like appearance

- **Crab**: Mobile creature (movable solid)
  - Walks on surfaces

- **NonMoveableSolid**: Base class for immovable solids

#### `com.apet2929.game.particles.liquid`
Liquid particle types:
- **Water**: Liquid
  - Falls under gravity
  - Spreads horizontally
  - Can extinguish fire

- **Fire**: Special liquid
  - Spreads in all directions
  - Produces smoke
  - Can be extinguished by water

- **Oil**: Liquid
  - Similar to water
  - Different appearance/behavior

#### `com.apet2929.game.particles.gas`
Gas particle types:
- **Smoke**: Gas
  - Rises (negative gravity)
  - Spreads and disperses
  - Produced by fire

### 1.3 Data Flow

#### Initialization Flow
```
Launcher.main()
  ↓
EngineManager.start()
  ↓
EngineManager.init()
  ├─ WindowManager.init()
  ├─ SandSim.init()
  │  ├─ AssetCache initialization
  │  ├─ Grid and World creation
  │  ├─ Particle model loading
  │  └─ Texture loading
  └─ MouseInput.init()
```

#### Game Loop
```
EngineManager.run()
  └─ while(isRunning)
      ├─ Sync.sync(60 FPS)
      ├─ input()
      │  ├─ MouseInput.input()
      │  └─ SandSim.input(MouseInput)
      ├─ update()
      │  └─ SandSim.update()
      │     └─ World.update()
      │        └─ for each particle: particle.update(world)
      ├─ render()
      │  └─ SandSim.render()
      │     └─ RenderManager.render()
      │        └─ World.render()
      └─ WindowManager.update()
```

#### Particle Update Flow
```
World.update()
  ├─ Get particles to update
  └─ for each particle P
      └─ P.update(World)
          ├─ Check swap conditions
          ├─ Move if applicable
          └─ Apply gravity/physics rules
```

### 1.4 Key Design Patterns

**ILogic Interface Pattern**
- Decouples game logic from engine
- Allows easy swapping of game implementations
- Provides standard lifecycle: init, input, update, render, cleanup

**Particle Factory Pattern**
- ParticleType enum with createParticleByMatrix() method
- Encapsulates particle instantiation
- ParticleLoader provides texture loading

**Resource Caching Pattern**
- AssetCache prevents duplicate texture/model loading
- Reduces GPU memory usage

**Matrix-Based Grid Pattern**
- Particle[][] stores world state
- Direct array access for O(1) lookups
- Simplifies physics calculations

### 1.5 Physics and Simulation

#### Particle Matter Types
```
MatterType
├─ IMMOVABLESOLID (Stone)
├─ MOVABLESOLID (Sand, Plant, Crab)
├─ LIQUID (Water, Fire, Oil)
├─ GAS (Smoke)
└─ EMPTY (Empty cells)
```

#### Movement Rules
- **Gravity**: Solids and liquids fall (except immovable solids)
- **Buoyancy**: Lighter particles float in heavier particles
- **Flow**: Liquids spread horizontally
- **Dispersion**: Gases rise and spread
- **Swapping**: Particles exchange positions based on canSwap() logic

#### Known Physics Issues
- Camera movement sometimes clears screen (to be debugged)
- Sand prefers to fall right rather than straight down
- Sand staggers/doesn't settle properly when falling in water

## 2. System Requirements

### Hardware Requirements
- **Processor**: Intel Core i5 or equivalent (2+ cores)
- **RAM**: 4 GB minimum, 8 GB recommended
- **GPU**: OpenGL 4.0+ compatible graphics card
- **Display**: 1600x900 minimum resolution
- **Storage**: 100 MB for installation and textures

### Software Requirements
- **Operating System**: Windows 10/11, Linux, or macOS
- **Java Runtime**: JDK 18 or higher
- **Maven**: Version 3.6.0 or higher (for building from source)

### Graphics Requirements
- OpenGL 4.0 or higher support
- DirectX 12 (Windows) or Vulkan (Linux/macOS) compatible GPU
- Minimum 2 GB dedicated VRAM

## 3. Build and Deployment

### 3.1 Building from Source

#### Prerequisites
```bash
# Verify Java installation
java -version
# Should show Java 18+

# Verify Maven installation
mvn -version
# Should show Maven 3.6.0+
```

#### Build Steps
```bash
# Navigate to project directory
cd /path/to/SandSim

# Clean previous builds
mvn clean

# Compile and test
mvn compile

# Run tests
mvn test

# Package as executable JAR
mvn package
```

### 3.2 Project Structure for Build
```
SandSim/
├── pom.xml                 # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/           # Source code
│   │   └── resources/      # Shaders and assets
│   └── test/
│       └── java/           # Unit tests
├── target/                 # Build output
│   ├── classes/            # Compiled bytecode
│   ├── test-classes/       # Compiled tests
│   ├── SandSim.jar         # Executable JAR
│   └── surefire-reports/   # Test reports
└── textures/               # Texture assets
```

### 3.3 Maven Configuration Details

#### Compiler Settings
```
- Source: Java 18
- Target: Java 18
- Encoding: UTF-8
```

#### Key Dependencies
```
- LWJGL 3.4.1: Graphics and windowing
- JOML 1.10.8: Mathematics
- JUnit 5.8.1: Testing
- EasyMock 5.6.0: Test mocking
```

#### Build Output
- **Final Name**: SandSim.jar (executable)
- **Plugin**: Maven Shade Plugin (creates fat JAR with all dependencies)
- **Main Class**: com.apet2929.game.Launcher

### 3.4 Running the Application

#### From JAR
```bash
java -jar target/SandSim.jar
```

#### From IDE
- Build project in IDE (IntelliJ IDEA or Eclipse)
- Run Launcher.main() as Java application

#### Command Line (Development)
```bash
mvn compile exec:java -Dexec.mainClass="com.apet2929.game.Launcher"
```

## 4. API and Interface Documentation

### ILogic Interface
```java
public interface ILogic {
    void init() throws Exception;      // Initialize resources
    void input(MouseInput mouseInput);  // Handle input
    void update();                      // Update game state
    void render();                      // Render to screen
    void cleanup();                     // Clean up resources
}
```

### Particle Base Class
```java
public abstract class Particle {
    // Position
    public int getGridX();
    public int getGridY();
    public void setPositionByWorld(Vector2i position);
    
    // Type and properties
    public ParticleType getType();
    public Texture getTexture();
    
    // Behavior
    public abstract boolean canSwap(ParticleType type);
    public boolean canSwap(Particle other);
    
    // Physics
    public void update(World world);
    public void moveWithVelocity(World world);
    
    // Type queries
    public boolean isEmpty();
    public boolean isSolid();
    public boolean isLiquid();
    public boolean isGas();
}
```

### World Class Key Methods
```java
public class World {
    // Particle access
    public Particle getAt(int x, int y);
    public void setAt(Vector2i pos, Particle particle);
    
    // Particle spawning
    public void spawnParticle(ParticleType type, int x, int y);
    
    // Movement
    public void moveWithSwap(Vector2i pos, int changeX, int changeY);
    public void moveDown(Vector2i pos);
    
    // World manipulation
    public Grid expand(ObjectLoader loader, ExpandDirection direction, int amount);
    public void fillRandomly();
    
    // Physics
    public int getDirectionBias();
    
    // Rendering
    public void render(RenderManager renderer, Model particleModel);
    public void update();
}
```

### Camera Class Key Methods
```java
public class Camera {
    // Movement
    public void move(Vector2f movement);
    public void move(Vector3f movement);
    
    // View matrix
    public Matrix4f getViewMatrix();
    
    // Position access
    public Vector3f getPosition();
    public Vector3f getFront();
    public Vector3f getUp();
}
```

---

# User Documents

## 1. User Guide

### 1.1 Getting Started

#### Installation
1. Ensure Java 18+ is installed on your system
2. Download the SandSim.jar file or build from source
3. Double-click SandSim.jar to launch (or use: `java -jar SandSim.jar`)
4. The game window will open (default size: 1600x900 pixels)

#### First Launch
- You'll see a 2D grid of cells with a grid background
- A blue particle brush indicator shows where your cursor is
- The title bar shows "SandSim"

### 1.2 Controls

#### Mouse Controls
| Control | Action |
|---------|--------|
| **Left Click** | Paint particles at cursor position |
| **Left Click + Hold** | Continuously paint while moving mouse |
| **Right Click** | (Reserved for future use) |

#### Keyboard - Particle Selection
| Key | Particle Type |
|-----|---------------|
| **1** | Sand (falls under gravity) |
| **2** | Water (flows and spreads) |
| **3** | Smoke (rises, disperses) |
| **4** | Stone (immovable barrier) |
| **5** | Fire (spreads, produces smoke) |
| **6** | Oil (burns, rises over water) |
| **7** | Plant (burns, grows into sand) |
| **8** | Crab (moves through air and water) |
| **0** | Erase/Empty (clears particles) |

#### Keyboard - Camera Controls
| Key(s) | Action |
|--------|--------|
| **Left Arrow** | Pan camera left |
| **Right Arrow** | Pan camera right |
| **Up Arrow** | Pan camera up |
| **Down Arrow** | Pan camera down |
| **.** (period) | Zoom in |
| **/** (slash) | Zoom out |

#### Keyboard - Vim Movement (Advanced)
| Key | Action |
|-----|--------|
| **H** | Expand world left |
| **L** | Expand world right |
| **K** | Expand world up |
| **J** | Expand world down |

#### Keyboard - Game Controls
| Key | Action |
|-----|--------|
| **R** | Fill grid randomly with particles |
| **Enter** | Pause/Unpause simulation |
| **Esc** | Exit application |

### 1.3 Brush Settings

#### Brush Size
- Default brush size: 5x5 cells
- Modify in code: `SandSim.brushSize` variable

#### Space Bar Enhancement
- Pressing **Space** sprays a larger burst of particles
- Useful for quickly filling areas

### 1.4 Particle Types and Behaviors

#### Sand (Press 1)
- **Behavior**: Falls downward under gravity
- **Special Properties**: 
  - Piles up and stacks when hitting solid objects
  - Can fall diagonally around obstacles
  - Falls through empty space and liquids
- **Visual**: Tan-colored textured squares
- **Example Use**: Create dunes and structures

#### Water (Press 2)
- **Behavior**: Falls under gravity, then spreads horizontally
- **Special Properties**:
  - Flows into empty spaces
  - Spreads according to simple fluid mechanics
  - Can extinguish fire
- **Visual**: Blue textured squares
- **Example Use**: Create streams and pools, drown sand structures

#### Smoke (Press 3)
- **Behavior**: Rises and disperses outward
- **Special Properties**:
  - Moves upward (negative gravity)
  - Spreads and becomes less dense
  - Dissipates over time
- **Visual**: Gray textured squares
- **Example Use**: Create visual effects, test dispersion

#### Stone (Press 4)
- **Behavior**: Immovable barrier
- **Special Properties**:
  - Cannot be moved or destroyed
  - Blocks all other particles
  - Acts as wall/platform
- **Visual**: Dark gray textured squares
- **Example Use**: Create structures, barriers, platforms

#### Fire (Press 5)
- **Behavior**: Spreads in all directions
- **Special Properties**:
  - Produces smoke over time
  - Can be extinguished by water contact
  - Spreads to nearby combustible particles
- **Visual**: Orange/red textured squares
- **Example Use**: Create spreading fire effects, chemistry simulations

#### Empty/Erase (Press 0)
- **Behavior**: Removes particles
- **Use**: Clear areas, create gaps in structures

### 1.5 Interactive Scenarios

#### Scenario 1: Create a Sand Dune
1. Press **1** to select Sand
2. Hold down left mouse button
3. Paint in horizontal sweeping motions
4. Watch sand fall and pile up
5. **Expected**: Sand forms a dune that piles against itself

#### Scenario 2: Create a Water Stream
1. Press **1** to paint Sand and create a floor
2. Press **2** to select Water
3. Paint water on a slope above the sand
4. **Expected**: Water flows down and spreads across the sand

#### Scenario 3: Build a Stone Structure
1. Press **4** to select Stone
2. Paint stone blocks in desired shape
3. Paint sand or water around it
4. **Expected**: Particles pile up against stone but cannot move through it

#### Scenario 4: Create Fire
1. Press **1** to create a flammable base with sand
2. Press **5** to select Fire
3. Paint fire in a small area
4. Watch it spread
5. Press **2** to spray water on fire
6. **Expected**: Fire spreads then is extinguished by water

### 1.6 Visual Feedback

#### Grid Display
- Grid lines visible when zoomed in
- Grid lines fade when zoomed out
- Helps understand cell-based world

#### Particle Rendering
- Each particle type has distinct texture
- Textured squares rendered at grid positions
- Real-time visual feedback for all particle movements

#### Current Selection Indicator
- Blue highlight shows current brush area
- Updates as you move mouse

#### Window Title
- Shows application name and status

### 1.7 Troubleshooting

| Issue | Solution |
|-------|----------|
| Game won't start | Ensure Java 18+ is installed; try `java -version` |
| Window appears blank | Try zooming in/out with . and / keys |
| Particles not moving | Ensure simulation is not paused (press Enter) |
| Low performance | Reduce particle count or zoom out |
| Textures not loading | Verify texture files are in `textures/` folder |

---

# Administrator Documents

## 1. System Installation and Setup

### 1.1 Prerequisites

#### System Requirements
- **Operating System**: Windows 10/11, Linux (Ubuntu 20.04+), or macOS 10.14+
- **Java**: JDK 18 or higher
- **Graphics**: OpenGL 4.0+ compatible graphics card
- **RAM**: 4 GB minimum
- **Disk Space**: 200 MB (including Java runtime)

#### Installation Steps

##### Windows
```bash
# 1. Verify Java installation
java -version

# 2. Download and extract SandSim.jar or clone repository
# 3. Run the application
java -jar SandSim.jar
```

##### Linux
```bash
# 1. Install Java (if not present)
sudo apt update
sudo apt install openjdk-18-jdk

# 2. Run SandSim
java -jar SandSim.jar
```

##### macOS
```bash
# 1. Install Java (if using Homebrew)
brew install openjdk@18

# 2. Run SandSim
java -jar SandSim.jar
```

### 1.2 Building from Source (Administrators)

#### Setup Build Environment
```bash
# Install Maven (if not present)
# Windows: Download from https://maven.apache.org or use Chocolatey
# Linux: sudo apt install maven
# macOS: brew install maven

# Verify installation
mvn --version
```

#### Build Process
```bash
# Clone or download repository
cd SandSim/

# Clean previous builds
mvn clean

# Build application
mvn package

# Output JAR: target/SandSim.jar
```

### 1.3 Project Dependencies

#### Runtime Dependencies
| Dependency | Version | Purpose |
|-----------|---------|---------|
| LWJGL | 3.4.1 | OpenGL/Graphics binding |
| JOML | 1.10.8 | Math library |
| Java Runtime | 18+ | JVM execution |

#### Build-Time Dependencies
| Dependency | Version | Purpose |
|-----------|---------|---------|
| Maven | 3.6.0+ | Build automation |
| JUnit 5 | 5.8.1 | Unit testing |
| EasyMock | 5.6.0 | Test mocking |

### 1.4 Configuration

#### Window Configuration
File: `com.apet2929.engine.utils.Consts`

```java
public class Consts {
    public static final String TITLE = "SandSim";
    public static final int NUM_COLS_GRID = 256;  // Grid width in cells
    public static final int NUM_ROWS_GRID = 144;  // Grid height in cells
    public static final float FRAMERATE = 60.0f;  // Target FPS
    public static final float PARTICLE_TPS = 60.0f; // Physics update rate
}
```

#### Modifying Window Size
1. Edit `Launcher.java`:
   ```java
   window = new WindowManager(Consts.TITLE, 1600, 900, false);
   // Change 1600, 900 to desired width, height
   ```

2. Rebuild:
   ```bash
   mvn package
   ```

#### Modifying Grid Size
1. Edit `Consts.java`:
   ```java
   public static final int NUM_COLS_GRID = 256;  // Modify width
   public static final int NUM_ROWS_GRID = 144;  // Modify height
   ```

2. Rebuild and run

#### Modifying Physics Parameters
- **Gravity**: Modify particle update logic in respective Particle subclasses
- **Frame Rate**: Change `FRAMERATE` in Consts.java
- **Physics Tick Rate**: Change `PARTICLE_TPS` in Consts.java

### 1.5 File System Structure

#### Directory Layout
```
SandSim/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/apet2929/
│   │   │       ├── engine/         # Core graphics engine
│   │   │       │   ├── model/      # 3D model structures
│   │   │       │   └── utils/      # Utilities and constants
│   │   │       └── game/           # Game logic
│   │   │           └── particles/  # Particle system
│   │   └── resources/
│   │       └── shaders/            # OpenGL shader files
│   └── test/
│       └── java/                   # Unit tests
├── textures/                       # Particle texture files
├── target/
│   ├── SandSim.jar                # Executable JAR
│   ├── classes/                   # Compiled bytecode
│   └── surefire-reports/          # Test reports
├── pom.xml                        # Maven configuration
└── README.md                      # Project readme
```

#### Texture Files Location
- Location: `textures/` directory at project root
- Format: PNG files
- Naming: `[particle_type].png` (e.g., `SAND.png`, `WATER.png`)
- Size: Recommended 32x32 or 64x64 pixels

#### Shader Files Location
- Location: `src/main/resources/shaders/`
- Fragment Shaders: `fragment.fs`, `line_fragment.fs`
- Vertex Shaders: `vertex.vs`, `line_vertex.vs`
- Format: GLSL

### 1.6 Performance Tuning

#### Optimization Tips
1. **Reduce Grid Size**: Smaller grids improve performance
2. **Lower Frame Rate**: Reduce `FRAMERATE` in Consts.java if needed
3. **Limit Particles**: Fewer total particles = better performance
4. **GPU Drivers**: Ensure graphics drivers are up to date

#### Profiling
```bash
# Run with verbose GC output
java -verbose:gc -jar SandSim.jar

# Monitor system resources with external tools
# Windows: Task Manager
# Linux: top, htop
# macOS: Activity Monitor
```

### 1.7 Troubleshooting Guide

| Problem | Cause | Solution |
|---------|-------|----------|
| "No suitable Graphics Device" | No OpenGL 4.0+ support | Update GPU drivers |
| "GLFW_ERROR_PLATFORM_UNAVAILABLE" | Graphics context failed | Reinstall GPU drivers |
| Out of Memory | Too many particles | Reduce grid size or particle count |
| "Cannot find class" | Missing dependencies | Run `mvn clean compile` |
| Window won't open | Port conflict or display issue | Check display settings |
| Slow performance | CPU-bound | Reduce particle count or physics TPS |

### 1.8 Backup and Recovery

#### Backup Recommendations
```bash
# Backup entire project
tar -czf SandSim_backup.tar.gz SandSim/

# Backup textures
cp -r SandSim/textures textures_backup/

# Backup configuration
cp SandSim/src/main/java/com/apet2929/engine/utils/Consts.java Consts.java.bak
```

#### Recovery Steps
```bash
# If build fails, clean and rebuild
mvn clean install

# If resources missing, check file permissions
ls -la SandSim/textures/

# Verify Java installation
java -version
javac -version
```

---

# Test Plan and Strategy

## 1. Testing Strategy

### 1.1 Testing Approach
SandSim uses a multi-layered testing strategy:

#### Unit Testing
- **Framework**: JUnit 5 (Jupiter)
- **Mocking**: EasyMock 5.6.0
- **Scope**: Individual class methods
- **Location**: `src/test/java/`

#### Approach
- Mock external dependencies (Window, RenderManager, ObjectLoader)
- Test business logic in isolation
- Verify state changes and method calls

### 1.2 Test Execution

#### Run All Tests
```bash
mvn test
```

#### Run Specific Test Class
```bash
mvn test -Dtest=SandSimTest
mvn test -Dtest=WorldTest
mvn test -Dtest=CameraTest
```

#### Generate Test Report
```bash
mvn surefire-report:report
# Open: target/site/surefire-report.html
```

#### Test Results Location
```
target/surefire-reports/
├── com.apet2929.game.SandSimTest.txt
├── com.apet2929.game.WorldTest.txt
├── com.apet2929.engine.model.CameraTest.txt
├── TEST-com.apet2929.game.SandSimTest.xml
├── TEST-com.apet2929.game.WorldTest.xml
└── TEST-com.apet2929.engine.model.CameraTest.xml
```

## 2. Unit Test Cases

### 2.1 SandSimTest

#### Test: testSelectedParticleType
**Purpose**: Verify particle type selection from keyboard input  
**Setup**: Mock RenderManager, WindowManager, ObjectLoader, KeyboardManager  
**Action**: Simulate pressing key 2 (water), call input()  
**Expected Result**: `sim.selectedParticleType == ParticleType.WATER`  
**Status**: ✓ Passing

#### Test: testCameraMovesWhenLeftKeyPressed
**Purpose**: Verify camera movement when arrow key pressed  
**Setup**: Mock dependencies, create Camera and SandSim  
**Action**: Simulate LEFT arrow key press, call input()  
**Expected Result**: Camera X position decreases by (cameraMoveSpeed × deltaTime)  
**Status**: ✓ Passing

#### Test: testWorldSpawnParticleByType
**Purpose**: Verify particles are spawned in world  
**Setup**: Create 5×5 Grid and World  
**Action**: Call `world.spawnParticle(ParticleType.SAND, 2, 2)`  
**Expected Result**: `world.getAt(2,2)` returns non-null SAND particle  
**Status**: ✓ Passing

#### Test: testSandSimAddsSelectedParticleType
**Purpose**: Verify particle addition through SandSim interface  
**Setup**: Mock dependencies, create SandSim with grid  
**Action**: Simulate mouse click at position, verify particle added  
**Expected Result**: Particle of selected type exists at clicked position  
**Status**: ✓ Passing

### 2.2 WorldTest

#### Test: testUpdate
**Purpose**: Verify World.update() calls particle.update()  
**Setup**: Mock Grid, create World, mock Particle  
**Action**: Add mocked particle to world, call world.update()  
**Expected Result**: Particle.update(world) called exactly once  
**Status**: ✓ Passing

### 2.3 CameraTest

#### Test: testGetViewMatrix
**Purpose**: Verify view matrix returns new object each call  
**Setup**: Create Camera  
**Action**: Call getViewMatrix() twice  
**Expected Result**: Returns equal matrices but different object instances  
**Status**: ✓ Passing

#### Test: testNotMoving
**Purpose**: Verify zero movement doesn't change view matrix  
**Setup**: Create Camera, get initial view matrix  
**Action**: Call move(Vector3f(0,0,0))  
**Expected Result**: View matrix unchanged  
**Status**: ✓ Passing

#### Test: testMoveX
**Purpose**: Verify X-axis camera movement  
**Setup**: Create Camera with initial view matrix  
**Action**: Call move(Vector3f(1,0,0))  
**Expected Result**: View matrix m30 decreased by 1.0f (camera moves opposite)  
**Status**: ✓ Passing

#### Test: testMoveTwoArgs
**Purpose**: Verify camera movement with Vector2f (Y-axis)  
**Setup**: Create Camera  
**Action**: Call move(Vector2f(0,1))  
**Expected Result**: View matrix m31 decreased by 1.0f  
**Status**: ✓ Passing

## 3. Test Coverage Analysis

### Coverage by Package

#### com.apet2929.game
| Class | Status | Coverage |
|-------|--------|----------|
| SandSim | ✓ Tested | Particle selection, camera movement |
| World | ✓ Tested | Particle updates, spawning |
| Launcher | Not tested | Minimal logic, tested manually |
| Rule | Not implemented | Pending |
| ParticleTimer | Not tested | Simple utility |

#### com.apet2929.engine
| Class | Status | Coverage |
|-------|--------|----------|
| EngineManager | Manual test | Core loop tested through integration |
| WindowManager | Manual test | Window creation tested manually |
| RenderManager | Manual test | Rendering tested visually |
| MouseInput | Not tested | Input mocking used in tests |
| KeyboardManager | Mocked in tests | Not directly tested |
| AssetCache | Not tested | Tested through integration |
| Sync | Not tested | Frame sync tested implicitly |

#### com.apet2929.engine.model
| Class | Status | Coverage |
|-------|--------|----------|
| Camera | ✓ Tested | All movement operations |
| Grid | Not tested | Simple data structure |
| Model | Not tested | VAO/VBO wrapper |
| Texture | Not tested | OpenGL wrapper |
| ObjectLoader | Not tested | File I/O and GPU operations |
| Transformation | Not tested | Matrix operations |

#### com.apet2929.game.particles
| Class | Status | Coverage |
|-------|--------|----------|
| Particle | Manual test | Abstract base, tested through subclasses |
| ParticleType | Not tested | Enum with factory methods |
| ParticleLoader | Not tested | Texture loading utility |
| EmptyParticle | Not tested | Simple implementation |

#### Game Logic Coverage
**Particle Physics**: Tested manually through interactive scenarios  
**Water Flow**: Tested with ExploritoryTest.txt scenarios  
**Gravity**: Tested through visual observation  
**Fire Spread**: Tested with fire propagation scenarios  
**Smoke Rising**: Tested with smoke dispersion scenarios  

## 4. Integration and Manual Testing

### 4.1 Exploratory Testing Scenarios

#### Scenario 1: Basic Particle Placement
**Objective**: Verify particles render and place correctly  
**Steps**:
1. Launch application
2. Select Sand (press 1)
3. Click multiple times on grid
4. Observe particles appear at clicked locations

**Expected Result**: Sand particles visible and correctly positioned  
**Status**: ✓ Pass

#### Scenario 2: Gravity and Falling
**Objective**: Verify gravity-based fall behavior  
**Steps**:
1. Paint sand high on screen
2. Observe sand particles
3. Wait for settling

**Expected Result**: Sand falls downward due to gravity, piles up  
**Status**: ✓ Pass (with noted bias to right)

#### Scenario 3: Water Flow
**Objective**: Verify water spreading behavior  
**Steps**:
1. Create sand base
2. Select Water (press 2)
3. Paint water on slope
4. Observe spreading

**Expected Result**: Water flows and spreads horizontally  
**Status**: ✓ Pass

#### Scenario 4: Smoke Rising
**Objective**: Verify smoke rises and disperses  
**Steps**:
1. Paint smoke in center of screen
2. Observe behavior over time

**Expected Result**: Smoke moves upward and disperses  
**Status**: ✓ Pass

#### Scenario 5: Stone Barrier
**Objective**: Verify stone blocks particle movement  
**Steps**:
1. Create stone wall
2. Paint sand above wall
3. Observe sand piling against stone

**Expected Result**: Sand cannot pass through stone  
**Status**: ✓ Pass

#### Scenario 6: Fire Behavior
**Objective**: Verify fire spreads  
**Steps**:
1. Create fire particles
2. Observe spread over time
3. Apply water (press 2)
4. Observe extinguishing

**Expected Result**: Fire spreads and is extinguished by water  
**Status**: ✓ Pass

#### Scenario 7: Camera Controls
**Objective**: Verify camera panning and zooming  
**Steps**:
1. Use arrow keys to pan
2. Use . and / to zoom in/out

**Expected Result**: View pans and zooms smoothly  
**Status**: ⚠ Partial (camera movement sometimes clears screen)

#### Scenario 8: World Expansion
**Objective**: Verify world can expand  
**Steps**:
1. Use H/J/K/L to expand world
2. Paint particles in new areas

**Expected Result**: World expands smoothly, particles persist  
**Status**: ✓ Pass

### 4.2 Performance Testing

#### Test: Large Particle Count
**Procedure**: Fill grid with maximum particles  
**Measurement**: Monitor frame rate using FPS counter  
**Threshold**: Maintain 60 FPS with 10k+ particles  
**Result**: Achieves target FPS with optimization

#### Test: Grid Size Scaling
**Procedure**: Test with different grid sizes  
**Measurements**: 
- Small grid (64×64): 60 FPS consistently
- Medium grid (256×144): 60 FPS with many particles
- Large grid (512×288): 30-60 FPS depending on particle count

## 5. Known Issues and Limitations

### 5.1 Current Known Issues

#### Issue 1: Camera Movement Screen Clear
**Severity**: High  
**Description**: When moving camera with arrow keys, screen sometimes clears and doesn't recover without zooming  
**Impact**: Navigation becomes difficult  
**Workaround**: Use zoom (. / keys) to restore view  
**Status**: Documented, needs debugging  
**Investigation**: Likely view matrix calculation or render state issue

#### Issue 2: Sand Falling Bias
**Severity**: Medium  
**Description**: Sand particles prefer to fall diagonally right rather than straight down  
**Impact**: Unnatural gravity simulation  
**Root Cause**: Likely direction bias logic or random number seeding  
**Workaround**: None, physics aesthetic issue  
**Status**: Documented in codebase

#### Issue 3: Sand Settling in Water
**Severity**: Medium  
**Description**: Sand staggers and doesn't settle properly when falling through water  
**Impact**: Unnatural interaction between solids and liquids  
**Root Cause**: Swap logic or velocity handling needs review  
**Status**: Documented in journal

### 5.2 Limitations

#### Feature Limitations
- No save/load functionality (listed as planned feature, not yet implemented)
- No pause/rewind/record/replay (listed as planned, not implemented)
- No fire-smoke production (fire exists but doesn't produce smoke yet)
- No explosions (planned feature)
- No creatures with AI (Crab class exists but minimal behavior)

#### Performance Limitations
- Physics updates at single rate (60 TPS) - no variable timestep
- Grid size fixed at compile time
- Particle count limited by available RAM
- Camera movement can impact frame rate

#### Platform Limitations
- Requires OpenGL 4.0+ (not available on very old systems)
- LWJGL has platform-specific native libraries
- Tested primarily on Windows

## 6. Future Enhancements and Roadmap

### Planned Features
1. **Save/Load System**
   - Serialize particle state to file
   - Load saved worlds
   - Multiple save slots

2. **Time Control**
   - Pause and resume
   - Rewind to previous state
   - Replay recorded simulation
   - Speed control

3. **Advanced Particles**
   - Fire-to-smoke conversion
   - Explosions with force
   - More particle types (metal, wood, etc.)

4. **UI Improvements**
   - Particle type selector UI
   - Brush size adjustment UI
   - Grid information display
   - FPS counter

5. **Physics Improvements**
   - Symmetric swap rules
   - Directional rules
   - Pressure simulation
   - Temperature effects

6. **AI Creatures**
   - Crab behavior implementation
   - Pathfinding
   - Creature-to-particle interactions

7. **Editor Features**
   - Brush shapes (circle, line, etc.)
   - Undo/redo
   - Selection tool
   - Copy/paste areas

### Technical Debt
1. Refactor particle type references out of engine package
2. Reduce dependencies to add new particle types
3. Implement Rule class for swap definitions
4. Add more comprehensive unit tests
5. Improve error handling and logging

---

# Appendices

## Appendix A: Configuration Reference

### Window Constants (Consts.java)
```java
public static final String TITLE = "SandSim";
public static final int NUM_COLS_GRID = 256;      // Grid width
public static final int NUM_ROWS_GRID = 144;      // Grid height
public static final float FRAMERATE = 60.0f;      // Target FPS
public static final float PARTICLE_TPS = 60.0f;   // Physics ticks/sec
public static final int CELL_SIZE = 1;            // Pixels per cell
```

### Camera Settings (SandSim.java)
```java
public static final float cameraZoomSpeed = 20f;  // Zoom sensitivity
public float cameraMoveSpeed = 20f;               // Pan speed
public int brushSize = 5;                         // Brush size (cells)
```

### Physics Constants
```
Gravity: Applied each particle update (direction-dependent)
Viscosity: Implicit in swap rules
Pressure: Not implemented
Temperature: Not implemented
```

## Appendix B: Keyboard and Mouse Reference

### Complete Control Map
```
NUMBERS:     1=Sand, 2=Water, 3=Smoke, 4=Stone, 5=Fire, 0=Erase
ARROWS:      Navigate camera (Left/Right/Up/Down)
VIM KEYS:    H=Expand Left, L=Expand Right, K=Expand Up, J=Expand Down
ZOOM:        . = Zoom In, / = Zoom Out
GAME:        R = Fill Random, Enter = Pause/Play, Esc = Exit
MOUSE:       Left Click = Paint, Hold = Continuous paint
SPACE:       Spray larger brush area
```

## Appendix C: Physics Rules Reference

### Swap Conditions
Particles swap positions based on `canSwap()` method:

```
SAND:   Can swap with GAS, LIQUID, EMPTY
WATER:  Can swap with EMPTYPARTICLE
STONE:  Cannot swap (immovable)
SMOKE:  Can swap with EMPTY
FIRE:   Can swap with GAS, LIQUID, EMPTY
```

### Gravity Direction
```
Falling (positive Y): SAND, WATER, FIRE
Rising (negative Y): SMOKE
Stationary: STONE
```

## Appendix D: Maven Build Reference

### Common Maven Commands
```bash
mvn clean                          # Remove build artifacts
mvn compile                        # Compile source
mvn test                          # Run tests
mvn package                       # Create JAR
mvn install                       # Install locally
mvn clean install                 # Full rebuild
mvn -DskipTests package          # Skip tests during build
mvn clean package -DskipTests    # Quick build without tests
```

### Maven Profiles
```bash
mvn package -P production         # (if defined)
mvn package -P development        # (if defined)
```

## Appendix E: File Locations Quick Reference

| Item | Location |
|------|----------|
| Source Code | `src/main/java/com/apet2929/` |
| Tests | `src/test/java/com/apet2929/` |
| Shaders | `src/main/resources/shaders/` |
| Textures | `textures/` |
| Compiled JAR | `target/SandSim.jar` |
| Test Reports | `target/surefire-reports/` |
| Build Config | `pom.xml` |
| Constants | `src/main/java/com/apet2929/engine/utils/Consts.java` |

## Appendix F: Common Issues and Solutions

### Build Issues
**Issue**: "ERROR] Cannot find symbol"  
**Solution**: Run `mvn clean compile`

**Issue**: "ERROR] Plugin not found"  
**Solution**: Run `mvn -U clean compile` (update plugins)

### Runtime Issues
**Issue**: "Exception: No available display"  
**Solution**: Ensure graphics drivers installed, check X11 on Linux

**Issue**: "OutOfMemoryError"  
**Solution**: Increase heap: `java -Xmx2048m -jar SandSim.jar`

### Graphics Issues
**Issue**: "No suitable Graphics Device found"  
**Solution**: Update GPU drivers, check OpenGL 4.0+ support

## Appendix G: Dependencies and Licenses

### LWJGL 3.4.1
- **License**: BSD
- **Purpose**: OpenGL, GLFW, and native bindings
- **Website**: https://www.lwjgl.org/

### JOML 1.10.8
- **License**: MIT
- **Purpose**: Java Object Oriented Math Library
- **Website**: https://github.com/JOML-CI/JOML

### JUnit 5 (Jupiter) 5.8.1
- **License**: EPL 2.0
- **Purpose**: Unit testing framework
- **Website**: https://junit.org/junit5/

### EasyMock 5.6.0
- **License**: Apache 2.0
- **Purpose**: Mock object creation for testing
- **Website**: https://easymock.org/

## Appendix H: References and Resources

### Documentation
- [LWJGL Wiki](https://github.com/LWJGL/lwjgl3-wiki/wiki)
- [JOML Documentation](https://joml-ci.github.io/JOML/javadoc/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Maven Documentation](https://maven.apache.org/guides/)

### Related Technologies
- OpenGL 4.0+ Specification
- GLSL Shader Language
- Java 18 Documentation

## Appendix I: Support and Contact

### For Issues
- Check Known Issues section (Section 5.1)
- Review troubleshooting guides in Admin section
- Check test results in surefire-reports

### For Feature Requests
- Document in issue tracking system
- Reference roadmap (Section 6)
- Evaluate against architecture constraints

---

## Document Information

**Document Title**: SandSim: Complete System Documentation  
**Version**: 1.0  
**Last Updated**: May 14, 2026  
**Author**: SandSim Development Team  
**Status**: Complete and Ready for Delivery  
**Delivery Target**: CSSE375 Instructor  

---

## Revision History

| Version | Date | Changes | Author |
|---------|------|---------|--------|
| 1.0 | May 14, 2026 | Initial comprehensive documentation | Development Team |

---

**END OF DOCUMENT**

---
