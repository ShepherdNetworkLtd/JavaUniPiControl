# **Java UniPi Control**#
*The pure Java way to access your UniPi*

What's this?
------------
This library uses http [Evok API](https://github.com/UniPiTechnology/evok) developed by [UniPi](http://unipi.technology/)'s authors and allows you to access your UniPi from any computer in the pure Java way.

What is it good for?
-------

For controlling your UniPi, of course. You can for example control your analogue output, switch relays, at listeners for digital input change (someone pressing your button) etc.

What can it do & plans for the future development?
-------
Right now, implemented are this parts:

 - Analogue Inputs
 - Analogue Output
 - Digital Inputs
 - Relays
 - Thermometer

In the future is planned adding more parts to operate with.

How to use
-------
Most basic examples are shown in [src/UniPiExample.java](https://github.com/esoadamo/JavaUniPiControl/blob/master/src/UniPiExample.java), but if as you wish, I'll write some basic here for you to:

By default, Evok uses port 80, so you can initialize your UniPi instance by typing

    UniPi unipi = new UniPi("raspberrypiIP");
If you wish use another port (e.g. 81):

    UniPi unipi = new UniPi("raspberrypiIP", 81);

Now we want to check if button (connected to DI01) is pressed:

    DigitalInput  button = new DigitalInput(unipi, 1);
	  if(button.isOn())
	    System.out.println("I am impressed. Someone is pressing the button just from the begining!");
	  else
	    System.out.println("Hi! I somebody there? If yes, please press the button and start me again.");
	 
Ok, but now we do not care if the button is pressed right now. We just want to be notified when somebody will press the button:

    DigitalInput  button = new DigitalInput(unipi, 1);
  	button.addListener(new PropertyChangeListener(){
	    @Override
	    public void valueChanged(UniProperty prop) {
		if(prop.isOn())
		    System.out.println("Hey! I was just pressed!");
		else
		    System.out.println("Ok, they left me alone again...");
	    }
	});
That's it for the basics. For other parts (Relay, AI, Thermometer...) it is pretty the same, so you can start developing, or you can check [documentation](https://github.com/esoadamo/JavaUniPiControl/tree/master/doc).
