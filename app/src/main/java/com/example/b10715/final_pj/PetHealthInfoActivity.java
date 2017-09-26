package com.example.b10715.final_pj;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PetHealthInfoActivity extends AppCompatActivity {
    TextView pet_name, pet_birth, pet_breed, pet_sex, pet_number, number;
    Button editbtn;
    private ImageView pet_img;
    String pet_id, pet_image, pet_species, pet_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_health_info);

        editbtn = (Button) findViewById(R.id.editbtn);
        pet_img = (ImageView) this.findViewById(R.id.pet_img);
        pet_name = (TextView) findViewById(R.id.name);
        pet_breed = (TextView) findViewById(R.id.breed);
        pet_sex = (TextView) findViewById(R.id.sex);
        pet_birth = (TextView) findViewById(R.id.birth);
        number = (TextView) findViewById(R.id.number);
        pet_number = (TextView) findViewById(R.id.pet_number);

        Intent intent = getIntent();
        pet_id = intent.getExtras().getString("pet_id");
        pet_image = intent.getExtras().getString("pet_image");
        pet_name.setText(intent.getExtras().getString("pet_name"));
        pet_birth.setText(intent.getExtras().getString("pet_birth"));
        pet_species = intent.getExtras().getString("pet_species");
        pet_breed.setText(intent.getExtras().getString("pet_breed"));
        pet_sex.setText(intent.getExtras().getString("pet_sex"));
        pet_weight = intent.getExtras().getString("pet_weight");
        pet_number.setText(intent.getExtras().getString("pet_number"));
        pet_img.setImageURI(Uri.parse(pet_image));

        editbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PetHealthInfoActivity.this, PetEditActivity.class);
                intent.putExtra("pet_id", pet_id);
                intent.putExtra("pet_name", pet_name.getText().toString());
                intent.putExtra("pet_breed", pet_breed.getText().toString());
                intent.putExtra("pet_sex", pet_sex.getText().toString());
                intent.putExtra("pet_birth", pet_birth.getText().toString());
                intent.putExtra("pet_species", pet_species);
                intent.putExtra("pet_weight", pet_weight);
                intent.putExtra("pet_number", pet_number.getText().toString());
                intent.putExtra("pet_image", pet_image);

                startActivity(intent);
            }
        });


    }
}