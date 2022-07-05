package lu.uni.mics.meetingregistration;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
//import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class ParticipantActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        EditText firstNameText = findViewById(R.id.firstname);

        EditText lastNameText = findViewById(R.id.lastname);

        EditText phoneText = findViewById(R.id.phone);

        EditText emailText = findViewById(R.id.email);

        Button submitButton = findViewById(R.id.button);

        submitButton.setOnClickListener(v -> submitButtonHandler(firstNameText, lastNameText, phoneText, emailText));
    }

    public void submitButtonHandler(EditText firstNameText, EditText lastNameText, EditText phoneText, EditText emailText) {
        Intent intent = getIntent();
        String eventName = intent.getStringExtra("event name");

        // check validity of fields
        // extract the entered data from the EditText
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String phoneNumber = phoneText.getText().toString();
        String emailAddress = emailText.getText().toString();

        if (firstName.isEmpty() || !Pattern.matches("[[:alpha:]-]+", firstName)) {
            Toast.makeText(this, "Please enter a valid first name!", Toast.LENGTH_LONG).show();
        }

        else if (lastName.isEmpty() || !Pattern.matches("[[:alpha:]-]+", lastName)) {
            Toast.makeText(this, "Please enter a valid last name!", Toast.LENGTH_LONG).show();
        }

        else if (phoneNumber.isEmpty() || !Pattern.matches("[[:digit:]]+", phoneNumber)) {
            Toast.makeText(this, "Please enter a valid phone number!", Toast.LENGTH_LONG).show();
        }

        else if (emailAddress.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            Toast.makeText(this, "Please enter a valid email address!", Toast.LENGTH_LONG).show();
        }

        else {
            writeNewParticipant(eventName, firstName, lastName, phoneNumber, emailAddress);
        }
    }

    public void writeNewParticipant(String eventName, String firstName, String lastName, String phoneNumber, String emailAddress) {
        Participant participant = new Participant(firstName, lastName, phoneNumber, emailAddress);

        mDatabase.child(eventName).child(participant.getLastName()).setValue(participant)
                .addOnSuccessListener(success -> Toast.makeText(ParticipantActivity.this, "Thank you for your participation! You are now registered to the event.", Toast.LENGTH_LONG).show())
                .addOnFailureListener(failure -> Toast.makeText(ParticipantActivity.this, "Whoops, something went wrong! Please try to register again or scan another event QR code.", Toast.LENGTH_LONG).show()
        );
        this.finish();

    }

}