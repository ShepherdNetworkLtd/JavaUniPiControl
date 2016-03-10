import java.io.IOException;
import aho.rpi.unipi.AnalogOutput;
import aho.rpi.unipi.Relay;
import aho.rpi.unipi.UniPart;
import aho.rpi.unipi.UniPi;

public class UniPiExample {
    static UniPi unipi = new UniPi("rapsberrypi", 81);

    public static void main(String[] args) throws Exception {

	blinkAllRelays();

	Thread.sleep(3000);

	showWhatRelay1Do();

	Thread.sleep(3000);

	printSomeInfoAboutAO();

	System.out.println();

	Thread.sleep(1000);

	doTheSameInSecondWay();

	System.out.println();

	Thread.sleep(1000);
	//
	System.out.println("ao means " + UniPi.parseUniPart("ao"));
    }

    static void blinkAllRelays() throws IOException, InterruptedException {
	for (int i = 0; i <= 8; i++) {
	    unipi.set(UniPart.RELAY, i, "value", 1);
	    Thread.sleep(1000);
	    unipi.set(UniPart.RELAY, i, "value", 0);
	    Thread.sleep(1000);
	}
	for (int i = 8; i >= 1; i--) {
	    unipi.set(UniPart.RELAY, i, "value", 0);
	    Thread.sleep(1000);
	}
    }

    static void showWhatRelay1Do() throws IOException, InterruptedException {
	Relay relay1 = new Relay(unipi, 1);
	relay1.setOn(true);
	Thread.sleep(5000);
	relay1.toggle();
    }

    static void printSomeInfoAboutAO() throws IOException {
	System.out.println(StringArray2String(unipi.get(UniPart.ANALOG_OUTPUT, 1, "")));
    }

    static void doTheSameInSecondWay() throws IOException {
	AnalogOutput ao = new AnalogOutput(unipi);
	System.out.println(StringArray2String(ao.getAllData()));
    }

    private static String StringArray2String(String[][] array) {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < array.length; i++) {
	    sb.append(i + ":\n");
	    for (int i1 = 0; i1 < array[i].length; i1++)
		sb.append("- " + array[i][i1] + '\n');
	}
	return sb.toString();
    }
}
