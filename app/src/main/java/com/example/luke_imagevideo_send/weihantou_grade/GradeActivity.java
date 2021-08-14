package com.example.luke_imagevideo_send.weihantou_grade;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.views.Header;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GradeActivity extends BaseActivity {

    @BindView(R.id.header)
    Header header;
    @BindView(R.id.spPipeLevel)
    Spinner spPipeLevel;
    @BindView(R.id.spPipeMaterial)
    Spinner spPipeMaterial;
    @BindView(R.id.etPipeThickness)
    EditText etPipeThickness;
    @BindView(R.id.etPipeOD)
    EditText etPipeOD;
    @BindView(R.id.etUserYear)
    EditText etUserYear;
    @BindView(R.id.etNextYear)
    EditText etNextYear;
    @BindView(R.id.etMaxWorkMPa)
    EditText etMaxWorkMPa;
    @BindView(R.id.etDefectLength)
    EditText etDefectLength;
    @BindView(R.id.etMinThickness)
    EditText etMinThickness;
    @BindView(R.id.spSelect)
    Spinner spSelect;
    @BindView(R.id.etNum)
    EditText etNum;
    @BindView(R.id.tvC)
    TextView tvC;
    @BindView(R.id.tvT)
    TextView tvT;
    @BindView(R.id.tvBPI)
    TextView tvBPI;
    @BindView(R.id.tvPL0)
    TextView tvPL0;
    @BindView(R.id.tvLevel)
    TextView tvLevel;
    boolean first = true;
    String pipeLeave = "",pipeMaterial = "",select = "";
    String pipeThickness = "", pipeOD = "", userYear = "", nextYear = "", maxWorkMPa = "", defectLength = "", minThickness = "";
    double maxWorkMPaData ,PL0NumData, defectLengthData, pipeODData, leftOther, CNumData, TNumData, pipeThicknessData, minThicknessData, DifferenceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        spinneronCliect();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_grade;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {
        setEditData();
    }

    private void setEditData() {
        if (etPipeThickness.getText().toString().trim().equals("")){
            Toast.makeText(this, "管道壁厚不能为空", Toast.LENGTH_SHORT).show();
        }else if (etPipeOD.getText().toString().trim().equals("")){
            Toast.makeText(this, "管道外径不能为空", Toast.LENGTH_SHORT).show();
        }else if (etUserYear.getText().toString().trim().equals("")){
            Toast.makeText(this, "已使用年数不能为空", Toast.LENGTH_SHORT).show();
        }else if (etNextYear.getText().toString().trim().equals("")){
            Toast.makeText(this, "下一周期年数不能为空", Toast.LENGTH_SHORT).show();
        }else if (etMaxWorkMPa.getText().toString().trim().equals("")){
            Toast.makeText(this, "最大工作压力不能为空", Toast.LENGTH_SHORT).show();
        }else if (etDefectLength.getText().toString().trim().equals("")){
            Toast.makeText(this, "缺陷环向长度不能为空", Toast.LENGTH_SHORT).show();
        }else if (etMinThickness.getText().toString().trim().equals("")){
            Toast.makeText(this, "缺陷附近最小壁厚不能为空", Toast.LENGTH_SHORT).show();
        }else {
            pipeThickness = etPipeThickness.getText().toString().trim();
            pipeOD = etPipeOD.getText().toString().trim();
            userYear = etUserYear.getText().toString().trim();
            nextYear = etNextYear.getText().toString().trim();
            maxWorkMPa = etMaxWorkMPa.getText().toString().trim();
            defectLength = etDefectLength.getText().toString().trim();
            minThickness = etMinThickness.getText().toString().trim();

            BigDecimal CData = new BigDecimal((Double.valueOf(pipeThickness)-Double.valueOf(minThickness))/Double.valueOf(userYear)*Double.valueOf(nextYear)).setScale(6,BigDecimal.ROUND_HALF_UP);
            tvC.setText(String.valueOf(CData));
            BigDecimal TData = new BigDecimal(Double.valueOf(minThickness)-Double.valueOf(String.valueOf(CData))).setScale(6,BigDecimal.ROUND_HALF_UP);
            tvT.setText(String.valueOf(TData));
            BigDecimal BPIData = new BigDecimal(Double.valueOf(defectLength)/Double.valueOf(pipeOD)/3.141592).setScale(6,BigDecimal.ROUND_HALF_UP);
            tvBPI.setText(String.valueOf(BPIData));
            double D = 0;
            BigDecimal PL0Data = null;
            if (pipeMaterial.equals("20#钢")){
                //20#钢屈服强度为245
                D = Double.valueOf(pipeOD)/2;
                PL0Data = new BigDecimal(2/Math.sqrt(3)*245*Math.log(D/(D-Double.valueOf(minThickness)+Double.valueOf(String.valueOf(CData))))).setScale(6,BigDecimal.ROUND_HALF_UP);
            }else if (pipeMaterial.equals("奥氏体不锈钢")){
                //奥氏体不锈钢屈服强度为310
                D = Double.valueOf(pipeOD)/2;
                PL0Data = new BigDecimal(2/Math.sqrt(3)*310*Math.log(D/(D-Double.valueOf(minThickness)+Double.valueOf(String.valueOf(CData))))).setScale(6,BigDecimal.ROUND_HALF_UP);
            }else if (pipeMaterial.equals("16MnR")){
                //16MnR钢屈服强度为320
                D = Double.valueOf(pipeOD)/2;
                PL0Data = new BigDecimal(2/Math.sqrt(3)*320*Math.log(D/(D-Double.valueOf(minThickness)+Double.valueOf(String.valueOf(CData))))).setScale(6,BigDecimal.ROUND_HALF_UP);
            }
            tvPL0.setText(String.valueOf(PL0Data)+"");

            maxWorkMPaData = Double.valueOf(maxWorkMPa);
            PL0NumData = Double.valueOf(String.valueOf(PL0Data));
            defectLengthData = Double.valueOf(defectLength);
            pipeODData = Double.valueOf(pipeOD);
            leftOther = defectLengthData/pipeODData/3.141592;
            CNumData = Double.valueOf(String.valueOf(CData));
            TNumData = Double.valueOf(String.valueOf(TData));
            pipeThicknessData = Double.valueOf(pipeThickness);
            minThicknessData = Double.valueOf(minThickness);
            if (select.equals("局部减薄")){
                DifferenceData = pipeThicknessData - minThicknessData;
                getLevel();
            }else if (select.equals("未焊透")){
                if (etNum.getText().toString().trim().equals("")){
                    Toast.makeText(this, "请输入未焊透值", Toast.LENGTH_SHORT).show();
                }else {
                    DifferenceData = Double.valueOf(etNum.getText().toString().trim());
                    getLevel();
                }
            }
            first = false;
        }
    }

    public void getLevel(){
        if(pipeLeave.equals("GC2 GC3")){
            if (maxWorkMPaData<PL0NumData*0.3){
                if (leftOther<=0.25){
                    if (0.40*TNumData- this.CNumData <= this.DifferenceData){
                        tvLevel.setText("4");
                    }if (this.DifferenceData <0.40*TNumData- this.CNumData && this.DifferenceData >0.33*TNumData- this.CNumData){
                        tvLevel.setText("3");
                    }if (0.33*TNumData- this.CNumData >= this.DifferenceData){
                        tvLevel.setText("2");
                    }
                }else if (0.25<leftOther&&leftOther<=0.75){
                    if (0.33*TNumData- this.CNumData <= this.DifferenceData){
                        tvLevel.setText("4");
                    }if (this.DifferenceData <0.33*TNumData- this.CNumData && this.DifferenceData >0.25*TNumData- this.CNumData){
                        tvLevel.setText("3");
                    }if (0.25*TNumData- this.CNumData >= this.DifferenceData){
                        tvLevel.setText("2");
                    }
                }else if (0.75<leftOther&&leftOther<=1.00){
                    if (0.25*TNumData- this.CNumData <= this.DifferenceData){
                        tvLevel.setText("4");
                    }if (this.DifferenceData <0.25*TNumData- this.CNumData && this.DifferenceData >0.2*TNumData- this.CNumData){
                        tvLevel.setText("3");
                    }if (0.2*TNumData- this.CNumData >= this.DifferenceData){
                        tvLevel.setText("2");
                    }
                }

            }else if (PL0NumData*0.3<maxWorkMPaData&& maxWorkMPaData<PL0NumData*0.5){
                if (leftOther<=0.25){
                    if (0.25*TNumData- this.CNumData <= this.DifferenceData){
                        tvLevel.setText("4");
                    }if (this.DifferenceData <0.25*TNumData- this.CNumData && this.DifferenceData >0.20*TNumData- this.CNumData){
                        tvLevel.setText("3");
                    }if (0.20*TNumData- this.CNumData >= this.DifferenceData){
                        tvLevel.setText("2");
                    }
                }else if (0.25<leftOther&&leftOther<=0.75||0.75<leftOther&&leftOther<=1.00){
                    if (0.20*TNumData- this.CNumData <= this.DifferenceData){
                        tvLevel.setText("4");
                    }if (this.DifferenceData <0.20*TNumData- this.CNumData && this.DifferenceData >0.15*TNumData- this.CNumData){
                        tvLevel.setText("3");
                    }if (0.15*TNumData- this.CNumData >= this.DifferenceData){
                        tvLevel.setText("2");
                    }
                }

            }
        }else if(pipeLeave.equals("GC1")){
            if (maxWorkMPaData<PL0NumData*0.3){
                if (leftOther<=0.25){
                    if (0.35*TNumData- this.CNumData <= this.DifferenceData){
                        tvLevel.setText("4");
                    }if (this.DifferenceData <0.35*TNumData- this.CNumData && this.DifferenceData >0.30*TNumData- this.CNumData){
                        tvLevel.setText("3");
                    }if (0.30*TNumData- this.CNumData >= this.DifferenceData){
                        tvLevel.setText("2");
                    }
                }else if (0.25<leftOther&&leftOther<=0.75){
                    if (0.30*TNumData- this.CNumData <= this.DifferenceData){
                        tvLevel.setText("4");
                    }if (this.DifferenceData <0.30*TNumData- this.CNumData && this.DifferenceData >0.20*TNumData- this.CNumData){
                        tvLevel.setText("3");
                    }if (0.20*TNumData- this.CNumData >= this.DifferenceData){
                        tvLevel.setText("2");
                    }
                }else if (0.75<leftOther&&leftOther<=1.00){
                    if (0.20*TNumData- this.CNumData <= this.DifferenceData){
                        tvLevel.setText("4");
                    }if (this.DifferenceData <0.20*TNumData- this.CNumData && this.DifferenceData >0.15*TNumData- this.CNumData){
                        tvLevel.setText("3");
                    }if (0.15*TNumData- this.CNumData >= this.DifferenceData){
                        tvLevel.setText("2");
                    }
                }

            }else if (PL0NumData*0.3<maxWorkMPaData&& maxWorkMPaData<PL0NumData*0.5){
                if (leftOther<=0.25){
                    if (0.20*TNumData- this.CNumData <= this.DifferenceData){
                        tvLevel.setText("4");
                    }if (this.DifferenceData <0.20*TNumData- this.CNumData && this.DifferenceData >0.15*TNumData- this.CNumData){
                        tvLevel.setText("3");
                    }if (0.15*TNumData- this.CNumData >= this.DifferenceData){
                        tvLevel.setText("2");
                    }
                }else if (0.25<leftOther&&leftOther<=0.75||0.75<leftOther&&leftOther<=1.00){
                    if (0.15*TNumData- this.CNumData <= this.DifferenceData){
                        tvLevel.setText("4");
                    }if (this.DifferenceData <0.15*TNumData- this.CNumData && this.DifferenceData >0.10*TNumData- this.CNumData){
                        tvLevel.setText("3");
                    }if (0.10*TNumData- this.CNumData >= this.DifferenceData){
                        tvLevel.setText("2");
                    }
                }

            }
        }
    };

    /**
     * spinner点击事件
     */
    private void spinneronCliect() {
        spPipeLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String[] languages = getResources().getStringArray(R.array.pipelevel);
                pipeLeave = languages[pos];
                if (!first){
                    setEditData();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        spPipeMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String[] languages = getResources().getStringArray(R.array.pipematerial);
                pipeMaterial = languages[pos];
                if (!first){
                    setEditData();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        spSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String[] languages = getResources().getStringArray(R.array.select);
                select = languages[pos];
                if (select.equals("局部减薄")){
                    etNum.setEnabled(false);
                }else if (select.equals("未焊透")){
                    etNum.setEnabled(true);
                }
                if (!first){
                    setEditData();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

    }
}