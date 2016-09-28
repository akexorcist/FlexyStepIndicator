package com.akexorcist.flexystepindicator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.akexorcist.library.flexystepindicator.FlexyStepIndicator;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, FlexyStepIndicator.StepClickListener {
    private FlexyStepIndicator siSomething;
    private Button btnOne;
    private Button btnTwo;
    private Button btnThree;
    private Button btnFour;
    private Button btnAllDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        setupView();
        setupThing();
    }

    private void bindView() {
        siSomething = (FlexyStepIndicator) findViewById(R.id.si_something);
        btnOne = (Button) findViewById(R.id.btn_step_one);
        btnTwo = (Button) findViewById(R.id.btn_step_two);
        btnThree = (Button) findViewById(R.id.btn_step_three);
        btnFour = (Button) findViewById(R.id.btn_step_four);
        btnAllDone = (Button) findViewById(R.id.btn_all_step_done);
    }

    private void setupView() {
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnAllDone.setOnClickListener(this);
    }

    private void setupThing() {
        List<Integer> descriptionList = Arrays.asList(
                R.string.step_1,
                R.string.step_2,
                R.string.step_3,
                R.string.step_4
        );
        siSomething.setStepDescriptionResourceList(descriptionList);
        siSomething.setStepClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnOne) {
            siSomething.setCurrentIndex(0);
        } else if (view == btnTwo) {
            siSomething.setCurrentIndex(1);
        } else if (view == btnThree) {
            siSomething.setCurrentIndex(2);
        } else if (view == btnFour) {
            siSomething.setCurrentIndex(3);
        } else if (view == btnAllDone) {
            siSomething.setAllStepDone();
        }
    }

    @Override
    public void onStepClick(int index) {
        // TODO Do something when step was clicked
    }
}

