package ds.edu.hearthstoneandriodapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
/**
 * HearthstoneInfo.java
 * Author: Kaizhong Ying
 * Andrew ID: kying
 * Last Modified: April 3, 2024
 *
 * An {@link AppCompatActivity} that serves as the main activity for an Android application designed to
 * fetch and display information about Hearthstone cards. It includes functionality to input a search term,
 * initiate a search for a Hearthstone card, and display the results including the card's image, flavor text,
 * and card text.
 * The activity uses an instance of {@link GetInfo} to perform the search operation asynchronously,
 * demonstrating a non-blocking way to perform network operations and update the UI with the results.
 */
public class HearthstoneInfo extends AppCompatActivity {

    HearthstoneInfo me = this; // Reference to this activity, used in callbacks

    /**
     * This method is learnt from lab AndroidInterestingPicture
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final HearthstoneInfo ma = this;// Another reference to this activity, used for callbacks

        Button submitButton = (Button)findViewById(R.id.submit);// Button to initiate the search


        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                String searchTerm = ((EditText)findViewById(R.id.searchTerm)).getText().toString();
                GetInfo gp = new GetInfo();
                gp.search(searchTerm, me, ma); // Done asynchronously in another thread.
                // It calls ip.pictureReady() in this thread when complete.
            }
        });
    }

    /**
     * Callback method that is called when the asynchronous search operation is complete. This method updates
     * the UI with the fetched Hearthstone card information, including displaying the card's image, flavor text,
     * and description text.
     *
     * @param picture The bitmap of the card image to display. If null, a default image or message is shown.
     * @param cardFlavor The flavor text of the card, to be displayed in a dedicated TextView.
     * @param cardText The descriptive text of the card's abilities or lore, also displayed in a TextView.
     */
    public void pictureReady(Bitmap picture, String cardFlavor, String cardText) {
        ImageView pictureView = (ImageView)findViewById(R.id.interestingPicture);
        EditText searchView = (EditText)findViewById(R.id.searchTerm);
        TextView statusMessage = (TextView)findViewById(R.id.statusMessage);
        TextView cardFlavorTextView = (TextView)findViewById(R.id.cardFlavorText);
        TextView cardTextTextView = (TextView)findViewById(R.id.cardText);

        String searchTerm = searchView.getText().toString(); // Retrieve the search term

        if (picture != null) {
            pictureView.setImageBitmap(picture);
            System.out.println("picture");
            pictureView.setVisibility(View.VISIBLE);
            statusMessage.setText("Here is a picture of a " + searchTerm);
            cardFlavorTextView.setText("The card flavor: " + "\n" + cardFlavor);
            cardTextTextView.setText("Desciption of skills: "  + "\n" + cardText); // Update the message indicating success
        } else {
            pictureView.setImageResource(R.mipmap.ic_launcher);
            System.out.println("No picture");
            pictureView.setVisibility(View.INVISIBLE);
            statusMessage.setText("Sorry, I could not find a picture of a " + searchTerm); // Update the message indicating failure
        }

        searchView.setText(""); // Clear the search term input field
        pictureView.invalidate(); // Ensure the ImageView updates

    }
}
