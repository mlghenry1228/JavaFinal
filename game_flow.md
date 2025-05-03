graph TD
    A[Start Game] --> B[Initialize: Set window, player ship, timers]
    B --> C[Game Loop: Every 16ms]
    
    C --> D[Handle Input]
    D --> E[Mouse Move/Drag: Update Player Ship X]
    D --> F[Mouse Left Press: Shoot every 200ms]
    D --> G[Spacebar Press: Shoot every 500ms]
    D --> H[Left/Right Key: Move Ship Left/Right]
    
    C --> I[Update Game State]
    I --> J[Every 1s: Spawn Enemy at random X, stop at random Y]
    I --> K[Every 2s: Random Enemy Shoots Red Bullet downward]
    I --> L[Update Player Bullets: Move up]
    I --> M[Update Enemy Bullets: Move down]
    I --> N[Update Enemies: Move down until stopped]
    
    C --> O[Check Collisions]
    O --> P{Player Bullet vs Enemy}
    P -->|Hit| Q[Remove Bullet & Enemy, Score +1]
    P -->|No Hit| R[Continue]
    O --> S{Enemy Bullet vs Player}
    S -->|Hit| T[Deduct 20 HP, Remove Bullet]
    S -->|No Hit| R
    
    C --> U{Is HP <= 0?}
    U -->|Yes| V[Stop Timers, Disable Input]
    V --> W[Display Game Over]
    W --> X[End Game]
    U -->|No| Y[Render Frame: Draw ship, enemies, bullets, score, HP]
    
    Y --> C