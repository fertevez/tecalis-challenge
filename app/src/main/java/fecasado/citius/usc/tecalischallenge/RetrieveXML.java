package fecasado.citius.usc.tecalischallenge;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * This AsyncTask makes XML queries, given an URL
 */
public class RetrieveXML extends AsyncTask {
    private String urlString;
    private int btnId;
    private Square square;
    private Customer customer = null;
    private Context context;

    public RetrieveXML(String urlString, int btnId, Square square, Context context) {
        this.urlString = urlString;
        this.square = square;
        this.context = context;
        this.btnId = btnId;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            URL url = new URL(urlString);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();

            // Get the XML from an input stream
            xpp.setInput(getInputStream(url), "UTF_8");

            // Build a Customer object from the retrieved XML data
            customer = new Customer();
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("ID")) {
                        xpp.next();
                        customer.setID(Short.parseShort(xpp.getText()));
                    } else if (xpp.getName().equalsIgnoreCase("FIRSTNAME")) {
                        xpp.next();
                        customer.setFirstName(xpp.getText());
                    } else if (xpp.getName().equalsIgnoreCase("LASTNAME")) {
                        xpp.next();
                        customer.setLastName(xpp.getText());
                    } else if (xpp.getName().equalsIgnoreCase("STREET")) {
                        xpp.next();
                        customer.setStreet(xpp.getText());
                    } else if (xpp.getName().equalsIgnoreCase("CITY")) {
                        xpp.next();
                        customer.setCity(xpp.getText());
                    }
                }
                eventType = xpp.next(); //move to next element
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customer;
    }

    @Override
    protected void onPostExecute(Object o) {
        customer = (Customer) o;
        // Link the customer with its corresponding square
        if (customer != null) {
            square.setCustomer(customer);
            square.setActivated(true);
        } else {
            Log.e("RetrieveXML", "Failed to get customer.");
            Toast.makeText(context,
                    "Failed to get information about customer " + btnId, Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private InputStream getInputStream(URL url) throws IOException {
        return url.openConnection().getInputStream();
    }

}