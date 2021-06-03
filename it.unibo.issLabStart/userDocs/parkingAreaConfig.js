const config = {
    floor: {
        size: { x: 31, y: 24                   }
    },
    player: {
         position: { x: 0.10, y: 0.16 },		//INIT
         speed: 0.2
    },
    sonars: [
       ],
    movingObstacles: [
    ],
   staticObstacles: [
        {
            name: "wallUp",
            centerPosition: { x: 0.44, y: 0.97},
            size: { x: 0.88, y: 0.01}
        },
        {
            name: "wallDown",
            centerPosition: { x: 0.44, y: 0.01},
            size: { x: 0.85, y: 0.01}
        },
        {
            name: "wallLeft",
            centerPosition: { x: 0.02, y: 0.48},
            size: { x: 0.01, y: 0.94}
        },
        {
            name: "wallRight",
            centerPosition: { x: 1.0, y: 0.5},
            size: { x: 0.01, y: 0.99}
        }
    ]
}

export default config;
