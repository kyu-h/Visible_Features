package com.example.b10715.final_pj;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class PetActivity extends Activity {

    String myJSON;
    String DATA_URL = Config.URL + "getdata_pet.php";

    private static final String TAG_RESULTS = "result";
    private static final String user_email = "user_email";
    private static final String pet_name = "pet_name";
    private static final String pet_birth = "pet_birth";
    private static final String pet_sex = "pet_sex";
    private static final String pet_species = "pet_species";
    private static final String pet_breed = "pet_breed";
    private static final String pet_weight = "pet_weight";
    private static final String pet_number = "pet_number";
    private static final String pet_id = "id";
    private String pet_image = "pet_image";
    private ImageView pet_img;

    Button petaddbtn, peteditbtn;
    TextView text;

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        list = (ListView) findViewById(R.id.listView);
        pet_img = (ImageView) this.findViewById(R.id.pet_img);
        petaddbtn = (Button) findViewById(R.id.pet_register);
     //   peteditbtn = (Button) findViewById(R.id.dialog);
        personList = new ArrayList<HashMap<String, String>>();
        getData(DATA_URL);


        list.setOnItemClickListener(listener);
        petaddbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PetActivity.this, PetRegisterActivity.class);
                startActivity(intent);
            }
        });

    }

   /* public void onClick(View v) {
        if (v.getId() == R.id.dialog) {

        }
    }*/


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                if (LoginActivity.call_email.equals(c.getString(user_email))) {
                    String id = c.getString(pet_id);
                    String email = c.getString(user_email);
                    String name = c.getString(pet_name);
                    String sex = c.getString(pet_sex);
                    String birth = c.getString(pet_birth);
                    String species = c.getString(pet_species);
                    String breed = c.getString(pet_breed);
                    String weight = c.getString(pet_weight);
                    String number = c.getString(pet_number);
                    String image = c.getString(pet_image);
                    HashMap<String, String> persons = new HashMap<String, String>();

                    persons.put(pet_id, id);
                    persons.put(user_email, email);
                    persons.put(pet_name, name);
                    persons.put(pet_birth, birth);
                    persons.put(pet_sex, sex);
                    persons.put(pet_species, species);
                    persons.put(pet_breed, breed);
                    persons.put(pet_weight, weight);
                    persons.put(pet_number, number);
                    persons.put(pet_image, image);
                    personList.add(persons);
                }
            }

            ListAdapter adapter = new SimpleAdapter(
                    PetActivity.this, personList, R.layout.list_item,
                    new String[]{pet_image, pet_name, pet_birth, pet_sex, pet_breed},
                    new int[]{R.id.pet_img, R.id.name, R.id.birth, R.id.sex, R.id.breed}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            text = (TextView) findViewById(R.id.email);

            Toast.makeText(PetActivity.this, personList.get(position).toString(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(PetActivity.this, PetHealthInfoActivity.class);
            intent.putExtra("pet_id", personList.get(position).get(pet_id));
            intent.putExtra("pet_name", personList.get(position).get(pet_name));
            intent.putExtra("pet_birth", personList.get(position).get(pet_birth));
            intent.putExtra("pet_breed", personList.get(position).get(pet_breed));
            intent.putExtra("pet_sex", personList.get(position).get(pet_sex));
            intent.putExtra("pet_species", personList.get(position).get(pet_species));
            intent.putExtra("pet_weight", personList.get(position).get(pet_weight));
            intent.putExtra("pet_number", personList.get(position).get(pet_number));
            intent.putExtra("pet_image", personList.get(position).get(pet_image));

            startActivity(intent);
        }
    };


    // php의 email을 가져오는 함수
    public void getEmail(int position) {
    }

    // php의 Data를 가져오는 함수
    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}

    /* 참조 : http://blog.naver.com/elder815/220875549924 */
  /* 코드 분석 : http://kin.naver.com/qna/detail.nhn?d1id=1&dirId=1040104&docId=263017102&qb=VVJMRW5jb2Rlci5lbmNvZGU=&enc=utf8&section=kin&rank=3&search_sort=0&spq=0 */
