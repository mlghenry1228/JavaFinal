stateDiagram-v2
    [*] --> MainMenu: 啟動遊戲
    state MainMenu {
        [*] --> ShowMenu: 顯示「星際大戰」標題、最高分
        ShowMenu --> StartGame: 點擊「開始遊戲」
        ShowMenu --> Exit: 點擊「離開遊戲」
        Exit --> [*]: 退出程式
        StartGame --> GameLoop: 重置遊戲、播放 bgm.wav
    }

    state GameLoop {
        [*] --> Initialize: 初始化（血量=3，得分=0，清空子彈/敵人）
        Initialize --> Update: 每16ms更新
        state Update {
            [*] --> PlayerActions
            PlayerActions --> MoveShip: 滑鼠移動/拖曳或左右鍵
            PlayerActions --> Shoot: 滑鼠左鍵/空白鍵\n播放 player_shoot.wav
            Shoot --> AddBullets: 添加直線子彈
            Shoot --> AddDiagonalBullets: 若得分≥100，添加±60°斜向子彈
            AddBullets --> UpdateBullets
            AddDiagonalBullets --> UpdateBullets

            UpdateBullets --> MoveBullets: 玩家子彈上移、斜向子彈按角度移動、敵人子彈下移
            MoveBullets --> RemoveOutOfBounds: 移除超出邊界子彈

            [*] --> SpawnEnemies: 每spawnCooldown ms生成敵人\n播放 enemy_shoot.wav
            SpawnEnemies --> EnemyActions
            EnemyActions --> MoveEnemies: 下降至100–200後左右移動，撞牆反彈
            EnemyActions --> EnemyShoot: 每1000–1500ms射擊\n子彈數=level+1\n播放 enemy_shoot.wav

            [*] --> Collisions
            Collisions --> PlayerHit: 敵人子彈擊中玩家\n血量-1
            Collisions --> EnemyHit: 玩家子彈/斜向子彈擊中敵人\n得分+1，移除敵人/子彈

            [*] --> LevelUp: 每50分（level=score/50）
            LevelUp --> IncreaseDifficulty: 顯示「難度提升！」1秒\n生成間隔縮短\n敵人子彈數+1\n血量+2（上限5）
        }

        Update --> GameOver: 血量≤0或敵人數>25（失敗）
        Update --> GameWin: 得分≥450（勝利）
        Update --> Update: 繼續迴圈
    }

    GameOver --> EndScreen: 停止音樂
    GameWin --> EndScreen: 停止音樂
    state EndScreen {
        [*] --> ShowEnd: 顯示「遊戲結束」或「你贏了！」\n得分、最高分
        ShowEnd --> Restart: 點擊「再來一場」\n重置遊戲、播放 bgm.wav
        ShowEnd --> MainMenu: 點擊「返回主畫面」
        ShowEnd --> Exit: 點擊「離開遊戲」
        Exit --> [*]: 退出程式
        Restart --> GameLoop
    }