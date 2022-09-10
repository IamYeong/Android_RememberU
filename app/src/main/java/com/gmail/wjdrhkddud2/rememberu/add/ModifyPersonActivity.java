package com.gmail.wjdrhkddud2.rememberu.add;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gmail.wjdrhkddud2.rememberu.HashConverter;
import com.gmail.wjdrhkddud2.rememberu.R;
import com.gmail.wjdrhkddud2.rememberu.SharedPreferencesManager;
import com.gmail.wjdrhkddud2.rememberu.db.RememberUDatabase;
import com.gmail.wjdrhkddud2.rememberu.db.person.Person;
import com.gmail.wjdrhkddud2.rememberu.detail.ModifyMemoFragment;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ModifyPersonActivity extends AppCompatActivity {

    private ImageButton backButton, bookmarkButton;
    private Button openBirthdayPickerButton, saveButton, maleButton, femaleButton;
    private EditText nameField, phoneField, descriptionField,
            birthYearField, birthMonthField, birthDayField;
    private TextView nameMessageText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_person);

        backButton = findViewById(R.id.img_btn_back_new_person);
        bookmarkButton = findViewById(R.id.img_btn_bookmark_add);
        saveButton = findViewById(R.id.btn_save_person);

        openBirthdayPickerButton = findViewById(R.id.btn_open_birthday_picker_new_person);

        nameField = findViewById(R.id.et_name_new_person);
        descriptionField = findViewById(R.id.et_description_new_person);
        phoneField = findViewById(R.id.et_phone_new_person);

        birthYearField = findViewById(R.id.et_birth_year_new_person);
        birthMonthField = findViewById(R.id.et_birth_month_new_person);
        birthDayField = findViewById(R.id.et_birth_day_new_person);

        maleButton = findViewById(R.id.btn_male_new);
        femaleButton = findViewById(R.id.btn_female_new);

        nameMessageText = findViewById(R.id.tv_new_person_name_message);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkButton.setSelected(!bookmarkButton.isSelected());
            }
        });

        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleButton.setSelected(true);
                femaleButton.setSelected(false);
            }
        });

        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleButton.setSelected(true);
                maleButton.setSelected(false);
            }
        });

        openBirthdayPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Person person = isExactly();

                if (person == null) return;

                RememberUDatabase db = RememberUDatabase.getInstance(ModifyPersonActivity.this);
                db.personDao().update(person);

                SharedPreferencesManager.setPersonHash(ModifyPersonActivity.this, person.getHashed());

                finish();

            }
        });


    }

    private Person isExactly() {

        RememberUDatabase db = RememberUDatabase.getInstance(ModifyPersonActivity.this);
        Person person = db.personDao().select(
                SharedPreferencesManager.getUID(ModifyPersonActivity.this),
                SharedPreferencesManager.getPersonHash(ModifyPersonActivity.this)
        );

        String name = nameField.getText().toString();
        String hash = "";
        String phone = phoneField.getText().toString();
        String description = descriptionField.getText().toString();
        String birth = birthYearField.getText().toString()
                + birthMonthField.getText().toString()
                + birthDayField.getText().toString();
        long birthDate = 0;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        try {

            Date date = simpleDateFormat.parse(birth);
            birthDate = date.getTime();
            hash = HashConverter.hashingFromString(
                    SharedPreferencesManager.getUID(ModifyPersonActivity.this)
                            + name
                            + phone
            );

        } catch (ParseException e) {

            return null;

        } catch (NoSuchAlgorithmException e) {

            return null;
        }

        person.setHashed(hash);
        person.setPhoneNumber(phone);
        person.setBookmark(bookmarkButton.isSelected());
        person.setName(name);
        person.setBirth(birthDate);
        person.setGender((maleButton.isSelected() ? 'm' : 'f'));
        person.setDescription(description);

        return person;

    }

}