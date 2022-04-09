.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

.. _Pierfrancesco Soffritti: https://github.com/PierfrancescoSoffritti/ConfigurableThreejsApp
.. _three.js : https://threejs.org/
.. _Node.js : https://nodejs.org/it/
.. _Docker Hub: https://hub.docker.com/
.. _DDR Robot: https://www.youtube.com/watch?v=aE7RQNhwnPQ

.. http://faculty.salina.k-state.edu/tim/robotics_sg/Control/kinematics/unicycle.html
.. https://www.epfl.ch/labs/la/wp-content/uploads/2018/08/Kappeler.Rapport.pdf.pdf
.. https://www.youtube.com/watch?v=ZekupxukiOM  Simulatore python  install pygame  https://www.youtube.com/watch?v=zHboXMY45YU

.. _Introduction to Docker and DockerCompose: ./_static/IntroDocker22.html

==========================================
VirtualRobot
==========================================

Unibo ha sviluppato un ambiente virtuale (denominato ``WEnv`` ) che include un robot 
che simula un Differential Drive robot (DDRRobot) reale. 

------------------------------------
Differential Drive robot 
------------------------------------

Un `DDR Robot`_ possiede due ruote motrici sullo stesso asse e una terza ruota condotta (non motrice).
La  tecnica *differential drive* consiste nel far muovere le ruote del robot a velocità
indipendenti l’una dall’altra.  

Nel seguito faremo riferimento a una forma semplificata di DDRobot in cui le possibìili mosse sono:

- muoversi avanti-indietro lungo una direzione costante
- fermarsi
- ruotare di 90° a destra o sinistra 

Queste mosse sono realizzate inviando opportuni comandi al robot (simulato o reale).

Per la costruzione di un DDR reale si veda xxx.
Al momento useremo una versione simulata, che descriviamo nella sezione :ref:`WEnv`.

Lo scopo non è certo quello di affrontare i problemi di progettazione tipci di un corso di robotica, ma quello di
introdurre casi di studio non banale per la costruzione di sistemi software distributi reattivi, proattivi e 
situati in un ambiente che può essere fonte di :ref:`Eventi`.


------------------------------------
WEnv
------------------------------------

L'ambiente virtuale WEnv  è stato realizzato principalmente da `Pierfrancesco Soffritti`_ utilizzando la 
libreria JavaScript `three.js`_. Il codice è disponibile nel progetto ``it.unibo.virtualRobot2020``.
 
Per attivare WEnv:

- Installare `Node.js`_
- In ``it.unibo.virtualRobot2020\node\WEnv\server``, eseguire **npm install**
- In ``it.unibo.virtualRobot2020\node\WEnv\WebGLScene``, eseguire **npm install**
- In ``it.unibo.virtualRobot2020\node\WEnv\server\src``, eseguire **node WebpageServer.js**
- Aprire un browser su node **localhost:8090**

++++++++++++++++++++++++++++++++++++
WEnv come immagine docker
++++++++++++++++++++++++++++++++++++

WEnv viene anche distribuito come immagine Docker.
    
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Dockerfile e creazione dell'immagine
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Il file di nome **Dockerfile** nella directory ``it.unibo.virtualRobot2020`` contiene le istruzioni per creare una 
immagine Docker (per una introduizione a Docker si veda `Introduction to Docker and DockerCompose`_).

.. code::

    FROM node:13-alpine
    RUN mkdir -p /home/node      
    EXPOSE 8090
    EXPOSE 8091
    COPY ./node/WEnv /home/node/WEnv 
    COPY ./node/WEnv/WebGLScene /home/node/WEnv/WebGLScene
    #set default dir so that next commands executes in it
    WORKDIR /home/node/WEnv/WebGLScene
    RUN npm install
    WORKDIR /home/node/WEnv/server
    RUN npm install
    WORKDIR /home/node/WEnv/server/src
    CMD ["node", "WebpageServer"]    

L'immagine Docker può essere creata sul proprio PC eseguendo il comando (nella directory che contiene il *Dockerfile*):

    ``docker build -t virtualrobotdisi:2.0 .``    //Notare il .

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Esecuzione della immagine
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

L'immagine Docker di WEnv può essere attivata sul PC con il comando:

.. code::

    docker run -ti -p 8090:8090 -p 8091:8091 --rm  virtualrobotdisi:2.0 /bin/sh
    node WebpageServer.js

Il comando:

    ``docker run -ti -p 8090:8090 -p 8091:8091 --rm  virtualrobotdisi:2.0 /bin/sh``

permette di ispezionare il contenuto della macchina virtuale e di attivare manualmente il sistema.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
virtualRobotOnly2.0.yaml
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

L'immagine viene resa distribuita  su `Docker Hub`_ nella immagine ``docker.io/natbodocker/virtualrobotdisi:2.0``
come risulta nella spefifica del file ``virtualRobotOnly2.0.yaml`` :

.. code::

    version: '3'
    services:
    wenv:
        image: docker.io/natbodocker/virtualrobotdisi:2.0
        ports:
        - 8090:8090
        - 8091:8091

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Esecuzione con docker-compose
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Questo file permette l'attivazione di WEnv attraverso l'uso di docker-compose:

.. code::

    docker-compose -f virtualRobotOnly2.0.yaml  up   //per attivare
    docker-compose -f virtualRobotOnly2.0.yaml  down //per terminare

++++++++++++++++++++++++++++++++++++
Scene per WEnv
++++++++++++++++++++++++++++++++++++

La scena del WEnv è costruita da una descrizione che può essere facilmente definita da un progettista di applicazioni. Un esempio (relativo alla scena a destra della figura seguente) può essere trovato in sceneConfig.js .





--------------------------------------------
MoveVirtualRobot
--------------------------------------------

- Con jupyter : resources\python\virtualrobotCaller.ipynb
- Invio di comandi tramite HTTP. Da rifare con Actor22 e supporti

.. code:: Java

    public class MoveVirtualRobot {
        private  final String localHostName    = "localhost";
        private  final int port                = 8090;
        private  final String URL              = "http://"+localHostName+":"+port+"/api/move";
    
        public MoveVirtualRobot() { }

        protected boolean sendCmd(String move, int time)  {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                System.out.println( move + " sendCmd "  );
                //String json         = "{\"robotmove\":\"" + move + "\"}";
                String json         = "{\"robotmove\":\"" + move + "\" , \"time\": " + time + "}";
                StringEntity entity = new StringEntity(json);
                HttpUriRequest httppost = RequestBuilder.post()
                        .setUri(new URI(URL))
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
                        .setEntity(entity)
                        .build();
                CloseableHttpResponse response = httpclient.execute(httppost);
                //System.out.println( "MoveVirtualRobot | sendCmd response= " + response );
                boolean collision = checkCollision(response);
                return collision;
            } catch(Exception e){
                System.out.println("ERROR:" + e.getMessage());
                return true;
            }
        }

        protected boolean checkCollision(CloseableHttpResponse response) throws Exception {
            try{
                //response.getEntity().getContent() is an InputStream
                String jsonStr = EntityUtils.toString( response.getEntity() );
                System.out.println( "MoveVirtualRobot | checkCollision_simple jsonStr= " +  jsonStr );
                //jsonStr = {"endmove":true,"move":"moveForward"}
                JSONObject jsonObj = new JSONObject(jsonStr) ;
                boolean collision = false;
                if( jsonObj.get("endmove") != null ) {
                    collision = ! jsonObj.get("endmove").toString().equals("true");
                    System.out.println("MoveVirtualRobot | checkCollision_simple collision=" + collision);
                }
                return collision;
            }catch(Exception e){
                System.out.println("MoveVirtualRobot | checkCollision_simple ERROR:" + e.getMessage());
                throw(e);
            }
        }

        public boolean moveForward(int duration)  { return sendCmd("moveForward", duration);  }
        public boolean moveBackward(int duration) { return sendCmd("moveBackward", duration); }
        public boolean moveLeft(int duration)     { return sendCmd("turnLeft", duration);     }
        public boolean moveRight(int duration)    { return sendCmd("turnRight", duration);    }
        public boolean moveStop(int duration)     { return sendCmd("alarm", duration);        }
    /*
    MAIN
    */
        public static void main(String[] args)   {
            MoveVirtualRobot appl = new MoveVirtualRobot();
            boolean moveFailed = appl.moveLeft(300);
            System.out.println( "MoveVirtualRobot | moveLeft  failed= " + moveFailed);
            moveFailed = appl.moveRight(1300);
            System.out.println( "MoveVirtualRobot | moveRight failed= " + moveFailed);
        }
        
    }