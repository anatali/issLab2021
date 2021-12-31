.. contents:: Overview
   :depth: 4
.. role:: red
.. role:: blue 
.. role:: remark
 

======================================
RaspberrySoftware
======================================

:remark:`Costa poco e permette di fare (e di imparare) molto.`

Riportiamo alcuni appunti per rendere più agevole l'attivazione e l'uso del RaspberryPi. 
Il modello di riferimento è il ``RaspberryPi 3 B+`` ma possono essere usati anche anche modelli
precedenti e ovviamente il ``RaspberryPi 4 B``.

------------------
Azioni Preliminari
------------------
.. C:\Didattica\Quantum\cidemo\userDocs\BrainCraftNat\Note\source
 

Appunti `a.a. 20-21 <../../../../../../it.unibo.raspIntro2020/userDocs/LabRaspiIntro.html>`_.

.. https://github.com/anatali/issLab2021/blob/master/it.unibo.issLabStart/

 
#. Crea scheda SD con ``2020-02-13-raspbian-buster-lite``.
#. Con la scheda inserita in un PC, crea un file vuoto ``boot/ssh``.
#. Crea un file ``boot/wpa_supplicant.conf`` con il seguente contenuto

 .. code::

    ctrl_interface=DIR=/var/run/wpa_supplicant GROUP=netdev
    update_config=1
    country=IT
    network={
        ssid="... Your_ESSID "
        psk="... Your_wifi_password"
        key_mgmt=WPA-PSK
    }  

A questo punto inserisci la scheda SD sul Raspberry, alimentalo
con ``5VDC 700mA`` e poi 
(Si veda anche `Raspberry Pi Wi-Fi & Bluetooth Setup <https://www.digikey.com/en/maker/blogs/raspberry-pi-wi-fi-bluetooth-setup-how-to-configure-your-pi-4-model-b-3-model-b>`_):

#. Leggi (opzionalmente collega il Rasperry con un cavo Ethernet) l'indirizzo IP del Raspberry sul Modem-HUB di casa.

#. Connetti il Raspberry via ``ssh`` usando ``Putty``.


Quando il sistema si attiva i file in boot è trasferito in
 ``etc/wpa_supplicant/wpa_supplicant.conf``


------------------------------
Uno sguardo al sistema
------------------------------
.. Uno sguardo al sistema di base `Raspbian GNU/Linux 10 (buster)`: 

Prima di procedere alla installazione di nuovo software, conviene dare un'occhiata alla versione iniziale

.. code::

    cat /etc/os-release   
        PRETTY_NAME="Raspbian GNU/Linux 10 (buster)"
        NAME="Raspbian GNU/Linux"
        VERSION_ID="10"
        ...

    uname -a
        Linux raspberrypi 4.19.118-v7+ #1311 SMP Mon Apr 27 14:21:24 BST 2020 armv7l GNU/Linux

Visualizziamo i dispositivi

.. code::

    arecord -l   #visualizza i dispositivi 
        **** List of CAPTURE Hardware Devices ****
        card 1: Camera [HD Web Camera], device 0: USB Audio [USB Audio]
    
    arecord -L  #visualizza in modo esteso 
        plughw:CARD=Camera,DEV=0
            HD Web Camera, USB Audio
            Hardware device with all software conversions

----------------------------------
Abilitazione
----------------------------------

.. code::

    sudo raspi-config

----------------------------------
Installazioni 2022
----------------------------------

+++++++++++++++++++++++++++++++++
Java
+++++++++++++++++++++++++++++++++

.. code::
    
    sudo apt install openjdk-8-jdk openjdk-8-jre
    update-alternatives --config java
    java -version
    -------------------JAVA HOME -----------------
    Include in /etc/environment
    JRE_HOME=/usr/lib/jvm/java-8-openjdk-armhf/jre/bin/java

Installazione di ``OpenJDK 11``, il default Java development e runtime nell'ultima versione di Raspbian OS, 
basata su Debian 10, Buster.

.. code::

    sudo apt update
    sudo apt install default-jdk
    java -version
+++++++++++++++++++++++++++++++++
GIT
+++++++++++++++++++++++++++++++++

.. code::	
    
    sudo apt-get install git
    git --version 	    #git version 2.20.1

+++++++++++++++++++++++++++++++++
 Node ed Express
+++++++++++++++++++++++++++++++++
Node.js è un runtime system open source multipiattaforma orientato agli eventi per l'esecuzione di codice JavaScript, 
costruita sul motore JavaScript V8 di Google Chrome. 
Molti dei suoi moduli base sono scritti in JavaScript, e gli sviluppatori possono scrivere nuovi moduli in JavaScript.

.. code::

    sudo apt remove nodejs nodejs-legacy -y
    sudo apt remove npm -y
    sudo apt remove --purge node

    //updates our Debian apt package repository 
    //to include the NodeSource packages
    curl -sL https://deb.nodesource.com/setup_13.x | sudo -E bash -
    sudo apt-get install -y nodejs  // to install Node.js 13.x and npm

    node -v				//v13.7.0
    npm --version		//6.13.6

    npm init 			//create package.json
    npm install serialport	//--save is implicit

    npm install express


+++++++++++++++++++++++++++++++++
 Samba
+++++++++++++++++++++++++++++++++
Samba consente la condivisione di file e stampe tra computer che eseguono Microsoft Windows e computer che eseguono Unix. 

.. code::

    sudo apt install -y samba

+++++++++++++++++++++++++++++++++
cmake
+++++++++++++++++++++++++++++++++
``cmake`` è un software multipiattaforma gratuito e open source per l'automazione della build, 
il test, il confezionamento e l'installazione di software utilizzando un metodo indipendente dal compilatore.
Ha dipendenze minime, richiedendo solo un compilatore C++ sul proprio sistema di compilazione.

.. code::

    sudo apt-get install cmake

+++++++++++++++++++++++++++++++++
aiocoap
+++++++++++++++++++++++++++++++++
Il package ``aiocoap`` è un'implementazione di ``CoAP`` (*Constrained Application Protocol*).
È scritto in Python 3 utilizzando i suoi metodi ``asyncio`` nativi per facilitare le operazioni simultanee 
mantenendo un'interfaccia facile da usare.


.. code::

    pip install aiocoap

.. https://aiocoap.readthedocs.io/en/latest/
.. wget https://aiocoap.readthedocs.io/en/latest/aiocoap-0.3.tar.gz
 .. tar xvzf aiocoap-0.3.tar.gz
.. cd aiocoap-0.3
.. sudo ./setup.py install

Si veda anche `Copper for Chrome (Cu4Cr) CoAP user-agent <https://github.com/mkovatsc/Copper4Cr>`_

+++++++++++++++++++++++++++++++++
Shellinabox
+++++++++++++++++++++++++++++++++

`Shellinabox <https://github.com/shellinabox/shellinabox>`_ 
utilizza la tecnologia ``AJAX`` per fornire l'aspetto di una shell nativa tramite un browser web. 
Il demone  ``shellinaboxd`` implementa un server web che ascolta sulla porta specificata 
(il defualt è ``4200``). 
Il server web pubblica uno o più servizi che verranno visualizzati in un  emulatore ``VT100`` 
implementato come applicazione web ``AJAX``. 

Shellinabox è incluso in molte distribuzioni Linux tramite repository predefiniti.
Per motivi di sicurezza, è bene cambiare la porta predefinita (ad esempio in 6754) 
per rendere difficile a chiunque raggiungere la casella SSH. 

Durante l'installazione viene creato automaticamente un nuovo certificato SSL autofirmato in 
``/var/lib/shellinabox`` per utilizzare il protocollo `HTTPS`.

.. code::
        
    sudo apt-cache search shellinabox
    sudo apt-get install openssl 
    sudo apt-get -y install shellinabox
    
    SHELLINABOX_PORT=6754                 #Cambio della porta
    sudo service shellinaboxd start       #Attiva il servizio
    sudo /etc/init.d/shellinabox restart

    /etc/init.d/shellinabox status
    sudo service --status-all             #visualizza i servizi
    sudo netstat -tlpn                    #visualizza i servizi and le porte TCP in ascolto
    sudo netstat -nap | grep shellinabox  #verifica

    https://localhost:4200

+++++++++++++++++++++++++++++++++
Ambiente virtuale Python
+++++++++++++++++++++++++++++++++

Un ambiente virtuale è uno strumento Python per la gestione delle dipendenze e 
l' isolamento del progetto. Consentono ai Package del sito Python (librerie di terze parti) 
di essere installati localmente in una directory isolata per un particolare progetto, 
invece di essere installati globalmente (cioè come parte di un Python a livello di sistema).

Posizionamoci in una drectory di lavoro e
creiamo un ambiente per Python 3 denominato **myenv**.

Fase 1: aggiungiamo un opzione di configurazione al file hidden denominato `.bashrc` aggiungendo il comando
(alias) `myenv`.

.. code::

    ( echo; echo '##### added for myenv #####'; echo 'export PATH=/home/pi/.local/bin:$PATH'; echo "alias myenv='source ~/myenv/bin/activate'") >> ~/.bashrc
    . ~/.bashrc
 
Fase 2: attiviamo il nuovo virtualenv e entriamo in esso:
 
.. code::

    pip3 install --upgrade pip
    python3 -m pip install virtualenv
    python3 -m virtualenv myenv

Per entrare nel virtualenv appena creato basta digitare:    
    
    ``myenv``

Per uscire dal virtualenv: 

    ``deactivate``.

+++++++++++++++++++++++++++++++++
Multi-Media
+++++++++++++++++++++++++++++++++

%%%%%%%%%%%%%%%%%%%%%%%%%%
Audio con alsa
%%%%%%%%%%%%%%%%%%%%%%%%%%

Nella directory `/home/pi/nat` inseriamo file wav ed mp3 ed eseguiamo (per sentire audio mediante cell-phone TRRS Headset):

.. code::

    aplay example_WAV.wav         #(non usare per mp3)
    omxplayer Oratore.mp3         #(- + regola volume)
    omxplayer example_WAV.wav     #riproduce - + modifica volumer
    omxplayer /home/pi/Music/Ella_Fitzgerald_Summertime.mp3

    speaker-test -c2 --test=wav -w Rear_Center.wav
    (cerca in /usr/share/sounds/alsa/xxx.wav)

   
Una libreria di suoni: https://pdsounds.org/

%%%%%%%%%%%%%%%%%%%%%%%%%%
RaspiCam
%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code::

    raspistill -o image.jpg             #scatta immagine
    raspivid -o video.h264 -t 5000		#crea video (in msecs)
 

%%%%%%%%%%%%%%%%%%%%%%%%%%
WebCam
%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code::

    sudo apt install fswebcam
    fswebcam image1.jpg                #crea immagine 320×240
    fswebcam -r 1280x720 image2.jpg    #crea immagine 1280x720


Se abbiamo installato una WebCam con microfono, controlliamone il funzionamento    

.. code::

    arecord -D plughw:Camera test.wav
    arecord -D plughw:Camera,0 -d 5 -f cd test.wav -c 2
    omxplayer test.wav      #riproduce - + modifica volumer

    alsamixer #F6 seleziona la scheda

%%%%%%%%%%%%%%%%%%%%%%%%%%
mjpg-streamer
%%%%%%%%%%%%%%%%%%%%%%%%%%

.. code::

    git clone https://github.com/jacksonliam/mjpg-streamer.git    
    sudo apt-get install cmake libjpeg8-dev
    cd mjpg-streamer-experimental
    make
    sudo make install
     ./start.sh     or bash start.sh
    
    http://localhost:8080/stream.html   

    /usr/local/bin/mjpg_streamer -i "input_uvc.so -r 1280x720  -d /dev/video0 -f 30" -o "output_http.so -p 8085  -w /usr/local/share/mjpg-streamer/www" 
    http://localhost:8085/stream.html   

..    export LD_LIBRARY_PATH=../mjpg_streamer -o "output_http.so -w ./www" -i "input_raspicam.so"

+++++++++++++++++++++++++++++++++
pygame
+++++++++++++++++++++++++++++++++
PyGame di solito viene installato con l'ultima distribuzione Raspbian
Pygame v1.9 is in raspi os at install.

.. code::

    /usr/local/lib #directory delle librerie
    
    sudo apt-get install libsdl-ttf2.0-0
    python3 -m pip install -U pygame --user #install in the home directory
    pygame.mixer.music.load("/home/pi/Music/Oratore.mp3")
    pygame.mixer.music.play(0)

    python 


+++++++++++++++++++++++++++++++++
opencv
+++++++++++++++++++++++++++++++++
L'ultima versione non sembra facilmente caricabile.

.. code::

    sudo apt install libatlas3-base
    pip install opencv-python==4.4.0.42
    
    python face_detection.py    #da: https://www.html.it/articoli/face-detection-python-10-linee-di-codice/
    Necessita scaricare         #da: https://github.com/opencv/opencv/tree/master/data/haarcascades


------------------
Docker
------------------

.. code::

    Installazione

        sudo curl -fsSL https://get.docker.com -o /tmp/get-docker.sh
        sudo chmod +x /tmp/get-docker.sh
        sudo sh /tmp/get-docker.sh

    Aggiunta permessi per eseguire comandi
        sudo usermod -aG docker pi 
            reboot !!!
        
        
        sudo dockerd &     #daemon runs with default configuration
        docker version	   #20.10.6
        docker info

    Test

        docker run hello-world 

------------------
Docker-compose
------------------

.. code::

    Installazione dipendenze
        sudo apt-get install -y libffi-dev libssl-dev
        sudo apt-get install -y python3 python3-pip
        sudo apt-get remove python-configparser 

    Installazione docker-compose
        sudo pip3 install docker-compose   


