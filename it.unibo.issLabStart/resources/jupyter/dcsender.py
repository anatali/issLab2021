{
 "cells": [
  {
   "cell_type": "code",
   "execution_count": 4,
   "metadata": {},
   "outputs": [
    {
     "name": "stdout",
     "output_type": "stream",
     "text": [
      "PLEASE ACTIVATE dcreciver ... \n",
      "CONNECTED  ('192.168.1.14', 8037)\n",
      "10\n",
      "INPUT 10\n",
      "forward  msg(cmd,dispatch,python,dcreceiver,cmd(10),1)\n",
      "20\n",
      "forward  msg(cmd,dispatch,python,dcreceiver,cmd(20),1)\n",
      "q\n"
     ]
    }
   ],
   "source": [
    "##############################################################\n",
    "# dcsender.ipynb\n",
    "# See C:\\Didattica2021\\issLab2021\\it.unibo.rasp2021\\src\\dcreceiver.qak\n",
    "##############################################################\n",
    "import socket\n",
    "import time\n",
    "\n",
    "port = 8037\n",
    "addr = '192.168.1.14'\n",
    "sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)\n",
    "\n",
    "cmdmsg  = \"msg(cmd,dispatch,python,dcreceiver,cmd(CMD),1)\"\n",
    " \n",
    "\n",
    "def connect(port) :\n",
    "    server_address = (addr, port)\n",
    "    sock.connect(server_address)    \n",
    "    print(\"CONNECTED \" , server_address)\n",
    "\n",
    "def forward( message ) :\n",
    "    print(\"forward \", message)\n",
    "    msg = message + \"\\n\"\n",
    "    byt=msg.encode()    #required in Python3\n",
    "    sock.send(byt)\n",
    "\n",
    "def emit( event ) :\n",
    "    print(\"emit \", event)\n",
    "    msg = event + \"\\n\"\n",
    "    byt=msg.encode()    #required in Python3\n",
    "    sock.send(byt)\n",
    "\n",
    "\n",
    "def terminate() :\n",
    "    sock.close()\n",
    "    print(\"BYE\")\n",
    "    \n",
    "def console() :  \n",
    "    v =  str( input() )\n",
    "    print(\"INPUT\" , v  )    \n",
    "    while( v != \"q\"  ) :\n",
    "        ## print( v )\n",
    "        forward(  cmdmsg.replace(\"CMD\", v) )\n",
    "        v = str(input() )     \n",
    "\n",
    "###########################################    \n",
    "print(\"PLEASE ACTIVATE dcreciver ... \")\n",
    "connect(port)\n",
    "console()\n",
    "##terminate()  \n"
   ]
  },
  {
   "cell_type": "code",
   "execution_count": null,
   "metadata": {},
   "outputs": [],
   "source": []
  }
 ],
 "metadata": {
  "kernelspec": {
   "display_name": "Python 3",
   "language": "python",
   "name": "python3"
  },
  "language_info": {
   "codemirror_mode": {
    "name": "ipython",
    "version": 3
   },
   "file_extension": ".py",
   "mimetype": "text/x-python",
   "name": "python",
   "nbconvert_exporter": "python",
   "pygments_lexer": "ipython3",
   "version": "3.8.8"
  }
 },
 "nbformat": 4,
 "nbformat_minor": 2
}
