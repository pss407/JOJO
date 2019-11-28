package com.example.jojo.bangguseok.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jojo.bangguseok.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class  SignIn extends AppCompatActivity {


    private EditText mEditTextName;
    private EditText mEditTextPassword;
    private TextView mTextViewResult;

    CheckBox check1; // 첫번쨰 동의
    CheckBox check2;

    Button button3,button4;

    String rid;
    String rpassword;

    private String mJsonString;

    boolean id_check = false;

    String cid="";

    public DatabaseReference mPostReference;

    public String ID;
    public String password;
    public ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mEditTextName = (EditText)findViewById(R.id.editText_main_name);
        mEditTextPassword = (EditText)findViewById(R.id.editText_main_country);
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        check1 = (CheckBox)findViewById(R.id.checkBox);
        check2 = (CheckBox)findViewById(R.id.checkBox2);




        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp=mEditTextName.getText().toString();

                if(id_check==false||!cid.equals(tmp))
                {
                    Toast toast=Toast.makeText(getApplicationContext(),"아이디 중복확인을 하셔야 합니다",Toast.LENGTH_LONG);
                    toast.show();
                    id_check=false;
                }
                else if(!check1.isChecked())
                {
                    Toast toast=Toast.makeText(getApplicationContext(),"이용약관동의를 체크해야 합니다",Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(!check2.isChecked())
                {
                    Toast toast=Toast.makeText(getApplicationContext(),"개인정보취급약관 동의를 체크해야 합니다",Toast.LENGTH_LONG);
                    toast.show();
                }
                else
                {
                    ID = mEditTextName.getText().toString();
                    password = mEditTextPassword.getText().toString();



                    postFirebaseDatabase(true);

                    /*
                    InsertData task = new InsertData();
                    task.execute("http://" + IP_ADDRESS + "/insert.php", name,password,"1","bronze");
                    */

                    mEditTextName.setText("");
                    mEditTextPassword.setText("");
                    finish();

                }
            }
        });
    }

    public void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){

            //Long l= new Long(31);
            FirebasePost post = new FirebasePost(ID, password, "bronze","false","false","0");
            postValues = post.toMap();
        }
        childUpdates.put("/id_list/" + ID, postValues);
        mPostReference.updateChildren(childUpdates);
    }



    public void onButton6Clicked(View v)
    {

        rid=mEditTextName.getText().toString();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean check=false;

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);
                    String[] info = {get.id, get.password, get.tier, get.using, get.start_matching};


                    if(info[0].equals(rid))
                    {
                        check=true;
                        break;
                    }


                }
                if(check==true)
                {

                    // 전역변수 설정 (로그인한 유저 정보들)

                    Toast toast=Toast.makeText(getApplicationContext(),"이미 사용하고있는 아이디입니다",Toast.LENGTH_LONG);

                    toast.show();


                }



            if(check==false)
            {
                Toast toast=Toast.makeText(getApplicationContext(),"사용가능한 아이디입니다.",Toast.LENGTH_LONG);
                cid = mEditTextName.getText().toString();
                toast.show();
                id_check=true;
            }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        };

        String sort_column_name = "id";
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("id_list").orderByChild(sort_column_name);
        sortbyAge.addListenerForSingleValueEvent(postListener);



    }


    public void onButton3Clicked(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이용약관 동의");
        builder.setMessage(
                "제1조 목적\n" +
                "\n" +
                "본 약관은 JOJO(이하 ‘회사’)가 스마트 기기를 통해 제공하는 모든 애플리케이션, 기타 제반 서비스(이하 ‘서비스’)를 이용하는 사용자(이하 ‘이용자’)와 회사 간에 서비스의 이용에 관한 권리, 의무 및 책임 사항, 기타 필요한 사항을 규정함을 목적으로 합니다.\n" +
                "\n" +
                "제2조 용어 정의\n" +
                "\n" +
                "① 본 약관에서 사용하는 용어의 정의는 다음과 같습니다.‘애플리케이션’이란 회사가 제공하는 서비스를 이용하기 위해 스마트기기를 통해 다운로드 받거나 설치하여 사용하는 프로그램 일체를 말합니다.‘이용자’이란 본 약관에 동의함을 전제로 회사가 제공하는 모든 애플리케이션 및 제반 서비스를 이용하는 자를 말합니다.‘스마트기기’란 콘텐츠를 다운로드 받거나 설치하여 이용하거나 네트워크를 통하여 이용할 수 있는 스마트폰, 태블릿 등의 네트워크를 사용할 수 있는 기기를 말합니다.‘콘텐츠’란 스마트 기기 등을 통하여 이용할 수 있도록 회사가 제공하는 애플리케이션 서비스와 관련되어 디지털 방식으로 제작된 텍스트, 사진, 그림 등 내용물 일체를 말합니다.\n" +
                "\n" +
                "② 본 약관에서 사용하는 용어의 정의는 본 조제 1항에서 정하는 것을 제외하고는 관계 법령에서 정하는 바에 의합니다. 관계 법령에서 정하지 않는 것은 일반적인 상 관례에 의합니다.\n" +
                "\n" +
                "제3조 약관의 효력 및 변경\n" +
                "\n" +
                "① 본 약관은 애플리케이션 소프트웨어 및 해당 애플리케이션의 모든 패치, 업데이트, 업그레이드 또는 새로운 버전에 적용되며, 가장 최신 버전이 모든 기존 버전에 우선합니다.\n" +
                "\n" +
                "② 본 약관은 서비스 이용계약의 성격상 회사의 웹사이트 또는 서비스 이용을 위한 애플리케이션 내에 본 약관을 명시하고, 이용자가 애플리케이션을 설치 및 실행함과 동시에 효력이 발생합니다.\n" +
                "\n" +
                "③ 회사는 관계 법령 또는 상관습에 위배되지 않는 범위에서 본 약관을 개정할 수 있습니다.\n" +
                "\n" +
                "④ 회사는 관련 법령의 변경이나 이용자의 권리 및 의무사항, 서비스 등을 개선하기 위해 본 약관을 변경할 수 있으며, 변경된 경우에 약관의 내용과 적용 일을 정하여, 적용일 7일 전 회사 공식 홈페이지나 공식 블로그, 제공하는 애플리케이션 등을 통해 이용자에게 고지하고 적용일부터 효력이 발생합니다.\n" +
                "\n" +
                "⑤ 이용자는 변경된 약관에 대해 거부할 권리가 있습니다. 본 약관의 변경에 대해 이의가 있는 이용자는 서비스 이용을 중단하고 이용을 해지(탈퇴 및 삭제)할 수 있습니다.\n" +
                "\n" +
                "⑥ 회사가 본 조 4항에 따라 변경된 약관을 공시 또는 고지하면서 이용자가 기간 내의 의사표시를 하지 않으면 변경된 약관에 동의한 것으로 간주합니다.\n" +
                "\n" +
                "제4조 약관의 해석\n" +
                "\n" +
                "본 약관은 회사가 제공하는 개별 서비스의 별도 정책과 함께 적용되며, 이에 명시되지 아니한 사항에 대해서는 별도의 약관 혹은 정책을 둘 수 있으며, 약관 및 정책은 정부가 제정한 관계 법령 또는 상 관례에 따릅니다.\n" +
                "\n" +
                "제5조 이용계약의 성립\n" +
                "\n" +
                "① 이용계약은 이용자가 본 이용약관에 대한 동의 또는 회사의 애플리케이션을 다운로드 받거나 실행하여 이용하는 경우 이 약관에 동의한 것으로 간주합니다.\n" +
                "\n" +
                "② 이용자는 동의하지 않는 경우 애플리케이션 계정 로그아웃 및 삭제와 함께 이를 철회할 수 있습니다.\n" +
                "\n" +
                "제6조 개인 정보의 보호 및 사용\n" +
                "\n" +
                "① 회사는 관계 법령이 정하는 바에 따라 이용자의 개인정보를 보호하기 위해 노력하며, 개인 정보의 보호 및 사용에 대해서는 관련 법령 및 회사의 개인정보 취급방침에 따릅니다. 단, 회사에서 제공하지 않는 서비스 및 애플리케이션에 대해서는 회사의 개인정보취급방침이 적용되지 않습니다.\n" +
                "\n" +
                "② 서비스의 특성에 따라 이용자의 닉네임, 프로필 사진과 같이 자신을 소개하는 내용이 공개될 수 있습니다.\n" +
                "\n" +
                "③ 회사는 본인확인을 위해 필요한 경우, 이용자에게 그 이유(용도)를 고지하고 이용자의 신분증 사본 또는 이에 준하는 서류를 요구할 수 있습니다. 회사는 이를 미리 고지한 목적 이외로 사용할 수 없으며, 목적 달성 시 지체 없이 파기합니다.\n" +
                "\n" +
                "④ 회사는 통신비밀보호법, 정보통신망법 등 관계 법령에 의해 관련 국가기관 등의 요구가 있는 경우를 제외하고는 이용자의 개인정보를 본인의 승낙 없이 타인에게 제공하지 않습니다.\n" +
                "\n" +
                "제7조 개인 정보의 관리 및 변경\n" +
                "\n" +
                "이용자는 본 서비스의 이용을 위해 자신의 개인 정보를 성실히 관리해야 하며, 개인정보에 변동사항이 있을 경우 이를 변경해야 합니다. 본 서비스를 이용하면서 이용자의 개인정보변경이 지연되거나 누락, 이용자에 의해 유출되어 발생하는 손해는 이용자의 책임으로 합니다.\n" +
                "\n" +
                "제8조 회사의 의무\n" +
                "\n" +
                "① 회사는 관련법과 본 약관의 금지하는 행위를 하지 않으며, 계속적이고 안정적인 서비스를 제공하기 위하여 최선을 다하여 노력합니다.\n" +
                "\n" +
                "② 회사는 이용자의 개인정보보호를 위한 보안 의무에 최선을 다합니다.\n" +
                "\n" +
                "③ 회사는 이용자로부터 제기되는 의견이나 불만이 정당하고 객관적으로 인정될 경우에는 적절한 절차를 거쳐 즉시 처리하여야 합니다. 다만, 즉시 처리가 불가한 경우에는 이용자에게 그 사유와 처리 일정을 통보하여야 합니다.\n" +
                "\n" +
                "제9조 이용자의 의무\n" +
                "\n" +
                "① 이용자는 본 약관에서 규정하는 사항과 기타 회사가 정한 제반 규정, 회사가 공지하는 사항을 준수하여야 합니다. 또한, 이용자는 회사의 업무에 방해가 되는 행위, 회사의 명예를 손상시키는 행위를 해서는 안 됩니다.\n" +
                "\n" +
                "② 이용자는 청소년보호법 등 관계 법령에 준수하여야 합니다. 이용자가 청소년 보호법 등 관계 법령을 위반한 경우는 해당 법령에 의거 처벌을 받게 됩니다.\n" +
                "\n" +
                "③ 이용자는 회사의 사전 승낙 없이 영리를 목적으로 서비스를 사용할 수 없으며, 이와 같은 행위로 회사에 손해를 끼친 경우, 이용자는 회사에 대한 손해배상 의무를 지며, 회사는 해당 이용자에 대해 서비스 이용 제한 및 적법한 절차를 거쳐 손해배상 등을 청구할 수 있습니다.\n" +
                "\n" +
                "④ 이용자의 닉네임에 관한 관리책임은 이용자에게 있으며, 이를 제3자가 이용하도록 하여서는 안 됩니다.\n" +
                "\n" +
                "⑤ 이용자는 다음 각호에 해당하는 행위를 하여서는 안 되며, 해당 행위를 하는 경우에 회사는 이용자의 서비스 이용제한, 관련 정보(글, 사진, 영상 등) 삭제 및 적법 조치를 포함한 이용제한 조치를 가할 수 있습니다. 또한, 그로 인해 발생한 문제의 책임은 이용자 본인에게 있습니다.\n" +
                "\n" +
                "(1) 각종 신청, 변경, 등록 시 허위의 내용을 등록하거나, 타인을 기만하는 행위\n" +
                "\n" +
                "(2) 타인의 정보를 도용한 행위\n" +
                "\n" +
                "(3) 회사로부터 특별한 권리를 받지 않고 애플리케이션을 역설계, 디컴파일, 분해, 임대, 재허락, 발행, 수정, 개조, 개작 또는 번역함, 2차적 저작물 작성, 다른 유저들이 네트워크에 접속하여 이용 가능하도록 하는 행위\n" +
                "\n" +
                "(4) 애플리케이션의 이전 버전을 재설치(이하 “다운그레이드”라고 함)하는 행위\n" +
                "\n" +
                "(5) 회사로부터 정식으로 인가된 배포 방식을 통하지 않고 다른 방식으로 애플리케이션을 배포 또는 사용하는 행위\n" +
                "\n" +
                "(6) 회사의 서버를 해킹하거나 웹사이트 또는 게시된 정보의 일부분 또는 전체를 임의로 변경하거나, 회사의 서비스를 비정상적인 방법으로 사용하는 행위\n" +
                "\n" +
                "(7) 회사의 애플리케이션 상의 버그를 악용하는 행위\n" +
                "\n" +
                "(8) 서비스에 위해를 가하거나 서비스를 고의로 방해하는 행위\n" +
                "\n" +
                "(9) 본 서비스를 통해 얻은 정보를 회사의 사전 승낙 없이 서비스 이용 외의 목적으로 복제하거나, 이를 출판 및 방송 등에 사용하거나, 제 3자에게 제공하는 행위\n" +
                "\n" +
                "(10) 타인의 특허, 상표, 영업비밀, 저작권, 기타 지적재산권을 침해하는 내용을 전송, 게시 또는 기타의 방법으로 타인에게 유포하는 행위\n" +
                "\n" +
                "(11) 청소년보호법 또는 법에 위반되는 저속, 음란한 내용의 정보, 문장, 도형, 음향, 동영상을 전송, 게시 또는 기타의 방법으로 타인에게 유포 하는 행위\n" +
                "\n" +
                "(12) 타인에게 불쾌감을 줄 수 있는 모욕적이거나 개인신상에 대한 내용이나 타인의 명예나 프라이버시를 침해할 수 있는 내용을 전송, 게시 또는 기타의 방법으로 타인에게 유포하는 행위\n" +
                "\n" +
                "(13) 다른 이용자를 희롱 또는 위협하거나, 특정 이용자에게 지속해서 고통 또는 불편을 주는 행위\n" +
                "\n" +
                "(14) 회사의 승인을 받지 않고 다른 사용자의 개인 정보를 수집 또는 저장하는 행위\n" +
                "\n" +
                "(15) 범죄와 결부된다고 객관적으로 판단되는 행위\n" +
                "\n" +
                "(16) 기타 관계 법령에 위배되는 행위\n" +
                "\n" +
                "제10조 서비스의 제공\n" +
                "\n" +
                "① 이용자가 본 약관에 대한 동의 또는 회사의 애플리케이션을 다운로드하거나 실행하여 이용하는 시점에 서비스 이용계약이 성립한 것으로 간주합니다. 단, 일부 서비스의 경우 회사의 필요에 따라 지정된 일자부터 서비스를 개시할 수 있습니다.\n" +
                "\n" +
                "② 회사는 이용자에게 본 약관에 정하고 있는 서비스를 포함하여 기타 부가적인 서비스를 함께 제공할 수 있습니다.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }

        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }




    public void onButton4Clicked(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("개인정보취급약관 동의");
        builder.setMessage("제1조(목적)"+"\n"+"이 약관은 OO 회사(전자상거래 사업자)가 운영하는 OO 사이버 몰(이하 “몰”이라 한다)에서 제공하는 인터넷 관련 서비스(이하 “서비스”라 한다)를 이용함에 있어 사이버 몰과 이용자의 권리․의무 및 책임사항을 규정함을 목적으로 합니다.\n" +
                "\n" +
                "  ※「PC통신, 무선 등을 이용하는 전자상거래에 대해서도 그 성질에 반하지 않는 한 이 약관을 준용합니다.」\n" +
                "\n" +
                "제2조(정의)\n" +
                "\n" +
                "  ① “몰”이란 OO 회사가 재화 또는 용역(이하 “재화 등”이라 함)을 이용자에게 제공하기 위하여 컴퓨터 등 정보통신설비를 이용하여 재화 등을 거래할 수 있도록 설정한 가상의 영업장을 말하며, 아울러 사이버몰을 운영하는 사업자의 의미로도 사용합니다.\n" +
                "\n" +
                "  ② “이용자”란 “몰”에 접속하여 이 약관에 따라 “몰”이 제공하는 서비스를 받는 회원 및 비회원을 말합니다.\n" +
                "\n" +
                "  ③ ‘회원’이라 함은 “몰”에 회원등록을 한 자로서, 계속적으로 “몰”이 제공하는 서비스를 이용할 수 있는 자를 말합니다.\n" +
                "\n" +
                "  ④ ‘비회원’이라 함은 회원에 가입하지 않고 “몰”이 제공하는 서비스를 이용하는 자를 말합니다.\n" +
                "\n" +
                "제3조 (약관 등의 명시와 설명 및 개정) \n" +
                "\n" +
                "  ① “몰”은 이 약관의 내용과 상호 및 대표자 성명, 영업소 소재지 주소(소비자의 불만을 처리할 수 있는 곳의 주소를 포함), 전화번호․모사전송번호․전자우편주소, 사업자등록번호, 통신판매업 신고번호, 개인정보관리책임자등을 이용자가 쉽게 알 수 있도록 00 사이버몰의 초기 서비스화면(전면)에 게시합니다. 다만, 약관의 내용은 이용자가 연결화면을 통하여 볼 수 있도록 할 수 있습니다.\n" +
                "\n" +
                "  ② “몰은 이용자가 약관에 동의하기에 앞서 약관에 정하여져 있는 내용 중 청약철회․배송책임․환불조건 등과 같은 중요한 내용을 이용자가 이해할 수 있도록 별도의 연결화면 또는 팝업화면 등을 제공하여 이용자의 확인을 구하여야 합니다.\n" +
                "\n" +
                "  ③ “몰”은 「전자상거래 등에서의 소비자보호에 관한 법률」, 「약관의 규제에 관한 법률」, 「전자문서 및 전자거래기본법」, 「전자금융거래법」, 「전자서명법」, 「정보통신망 이용촉진 및 정보보호 등에 관한 법률」, 「방문판매 등에 관한 법률」, 「소비자기본법」 등 관련 법을 위배하지 않는 범위에서 이 약관을 개정할 수 있습니다.\n" +
                "\n" +
                "  ④ “몰”이 약관을 개정할 경우에는 적용일자 및 개정사유를 명시하여 현행약관과 함께 몰의 초기화면에 그 적용일자 7일 이전부터 적용일자 전일까지 공지합니다. 다만, 이용자에게 불리하게 약관내용을 변경하는 경우에는 최소한 30일 이상의 사전 유예기간을 두고 공지합니다.  이 경우 \"몰“은 개정 전 내용과 개정 후 내용을 명확하게 비교하여 이용자가 알기 쉽도록 표시합니다. \n" +
                "\n" +
                "  ⑤ “몰”이 약관을 개정할 경우에는 그 개정약관은 그 적용일자 이후에 체결되는 계약에만 적용되고 그 이전에 이미 체결된 계약에 대해서는 개정 전의 약관조항이 그대로 적용됩니다. 다만 이미 계약을 체결한 이용자가 개정약관 조항의 적용을 받기를 원하는 뜻을 제3항에 의한 개정약관의 공지기간 내에 “몰”에 송신하여 “몰”의 동의를 받은 경우에는 개정약관 조항이 적용됩니다.\n" +
                "\n" +
                "  ⑥ 이 약관에서 정하지 아니한 사항과 이 약관의 해석에 관하여는 전자상거래 등에서의 소비자보호에 관한 법률, 약관의 규제 등에 관한 법률, 공정거래위원회가 정하는 전자상거래 등에서의 소비자 보호지침 및 관계법령 또는 상관례에 따릅니다.\n" +
                "\n" +
                "제4조(서비스의 제공 및 변경) \n" +
                "\n" +
                "  ① “몰”은 다음과 같은 업무를 수행합니다.\n" +
                "\n" +
                "    1. 재화 또는 용역에 대한 정보 제공 및 구매계약의 체결\n" +
                "    2. 구매계약이 체결된 재화 또는 용역의 배송\n" +
                "    3. 기타 “몰”이 정하는 업무\n" +
                "\n" +
                "  ② “몰”은 재화 또는 용역의 품절 또는 기술적 사양의 변경 등의 경우에는 장차 체결되는 계약에 의해 제공할 재화 또는 용역의 내용을 변경할 수 있습니다. 이 경우에는 변경된 재화 또는 용역의 내용 및 제공일자를 명시하여 현재의 재화 또는 용역의 내용을 게시한 곳에 즉시 공지합니다.\n" +
                "\n" +
                "  ③ “몰”이 제공하기로 이용자와 계약을 체결한 서비스의 내용을 재화등의 품절 또는 기술적 사양의 변경 등의 사유로 변경할 경우에는 그 사유를 이용자에게 통지 가능한 주소로 즉시 통지합니다.\n" +
                "\n" +
                "  ④ 전항의 경우 “몰”은 이로 인하여 이용자가 입은 손해를 배상합니다. 다만, “몰”이 고의 또는 과실이 없음을 입증하는 경우에는 그러하지 아니합니다.\n" +
                "\n" +
                "제5조(서비스의 중단) \n" +
                "\n" +
                "  ① “몰”은 컴퓨터 등 정보통신설비의 보수점검․교체 및 고장, 통신의 두절 등의 사유가 발생한 경우에는 서비스의 제공을 일시적으로 중단할 수 있습니다.\n" +
                "\n" +
                "  ② “몰”은 제1항의 사유로 서비스의 제공이 일시적으로 중단됨으로 인하여 이용자 또는 제3자가 입은 손해에 대하여 배상합니다. 단, “몰”이 고의 또는 과실이 없음을 입증하는 경우에는 그러하지 아니합니다.\n" +
                "\n" +
                "  ③ 사업종목의 전환, 사업의 포기, 업체 간의 통합 등의 이유로 서비스를 제공할 수 없게 되는 경우에는 “몰”은 제8조에 정한 방법으로 이용자에게 통지하고 당초 “몰”에서 제시한 조건에 따라 소비자에게 보상합니다. 다만, “몰”이 보상기준 등을 고지하지 아니한 경우에는 이용자들의 마일리지 또는 적립금 등을 “몰”에서 통용되는 통화가치에 상응하는 현물 또는 현금으로 이용자에게 지급합니다.\n" +
                "\n" +
                "제6조(회원가입) \n" +
                "\n" +
                "  ① 이용자는 “몰”이 정한 가입 양식에 따라 회원정보를 기입한 후 이 약관에 동의한다는 의사표시를 함으로서 회원가입을 신청합니다.\n" +
                "\n" +
                "  ② “몰”은 제1항과 같이 회원으로 가입할 것을 신청한 이용자 중 다음 각 호에 해당하지 않는 한 회원으로 등록합니다.\n" +
                "\n" +
                "    1. 가입신청자가 이 약관 제7조제3항에 의하여 이전에 회원자격을 상실한 적이 있는 경우, 다만 제7조제3항에 의한 회원자격 상실 후 3년이 경과한 자로서 “몰”의 회원재가입 승낙을 얻은 경우에는 예외로 한다.\n" +
                "    2. 등록 내용에 허위, 기재누락, 오기가 있는 경우\n" +
                "    3. 기타 회원으로 등록하는 것이 “몰”의 기술상 현저히 지장이 있다고 판단되는 경우\n" +
                "\n" +
                "  ③ 회원가입계약의 성립 시기는 “몰”의 승낙이 회원에게 도달한 시점으로 합니다.\n" +
                "\n" +
                "  ④ 회원은 회원가입 시 등록한 사항에 변경이 있는 경우, 상당한 기간 이내에 “몰”에 대하여 회원정보 수정 등의 방법으로 그 변경사항을 알려야 합니다.\n" +
                "\n" +
                "제7조(회원 탈퇴 및 자격 상실 등) \n" +
                "\n" +
                "  ① 회원은 “몰”에 언제든지 탈퇴를 요청할 수 있으며 “몰”은 즉시 회원탈퇴를 처리합니다.\n" +
                "\n" +
                "  ② 회원이 다음 각 호의 사유에 해당하는 경우, “몰”은 회원자격을 제한 및 정지시킬 수 있습니다.\n" +
                "\n" +
                "    1. 가입 신청 시에 허위 내용을 등록한 경우\n" +
                "    2. “몰”을 이용하여 구입한 재화 등의 대금, 기타 “몰”이용에 관련하여 회원이 부담하는 채무를 기일에 지급하지 않는 경우\n" +
                "    3. 다른 사람의 “몰” 이용을 방해하거나 그 정보를 도용하는 등 전자상거래 질서를 위협하는 경우\n" +
                "    4. “몰”을 이용하여 법령 또는 이 약관이 금지하거나 공서양속에 반하는 행위를 하는 경우\n" +
                "\n" +
                "  ③ “몰”이 회원 자격을 제한․정지 시킨 후, 동일한 행위가 2회 이상 반복되거나 30일 이내에 그 사유가 시정되지 아니하는 경우 “몰”은 회원자격을 상실시킬 수 있습니다.\n" +
                "\n" +
                "  ④ “몰”이 회원자격을 상실시키는 경우에는 회원등록을 말소합니다. 이 경우 회원에게 이를 통지하고, 회원등록 말소 전에 최소한 30일 이상의 기간을 정하여 소명할 기회를 부여합니다.\n" +
                "\n" +
                "제8조(회원에 대한 통지)\n" +
                "\n" +
                "  ① “몰”이 회원에 대한 통지를 하는 경우, 회원이 “몰”과 미리 약정하여 지정한 전자우편 주소로 할 수 있습니다.\n" +
                "\n" +
                "  ② “몰”은 불특정다수 회원에 대한 통지의 경우 1주일이상 “몰” 게시판에 게시함으로서 개별 통지에 갈음할 수 있습니다. 다만, 회원 본인의 거래와 관련하여 중대한 영향을 미치는 사항에 대하여는 개별통지를 합니다.\n" +
                "\n" +
                "제9조(구매신청 및 개인정보 제공 동의 등) \n" +
                "\n" +
                "  ① “몰”이용자는 “몰”상에서 다음 또는 이와 유사한 방법에 의하여 구매를 신청하며, “몰”은 이용자가 구매신청을 함에 있어서 다음의 각 내용을 알기 쉽게 제공하여야 합니다. \n" +
                "      1. 재화 등의 검색 및 선택\n" +
                "      2. 받는 사람의 성명, 주소, 전화번호, 전자우편주소(또는 이동전화번호) 등의 입력\n" +
                "      3. 약관내용, 청약철회권이 제한되는 서비스, 배송료․설치비 등의 비용부담과 관련한 내용에 대한 확인\n" +
                "      4. 이 약관에 동의하고 위 3.호의 사항을 확인하거나 거부하는 표시\n" +
                "         (예, 마우스 클릭)\n" +
                "      5. 재화등의 구매신청 및 이에 관한 확인 또는 “몰”의 확인에 대한 동의\n" +
                "      6. 결제방법의 선택\n" +
                "\n" +
                "  ② “몰”이 제3자에게 구매자 개인정보를 제공할 필요가 있는 경우 1) 개인정보를 제공받는 자, 2)개인정보를 제공받는 자의 개인정보 이용목적, 3) 제공하는 개인정보의 항목, 4) 개인정보를 제공받는 자의 개인정보 보유 및 이용기간을 구매자에게 알리고 동의를 받아야 합니다. (동의를 받은 사항이 변경되는 경우에도 같습니다.)\n" +
                "\n" +
                "\n" +
                "  ③ “몰”이 제3자에게 구매자의 개인정보를 취급할 수 있도록 업무를 위탁하는 경우에는 1) 개인정보 취급위탁을 받는 자, 2) 개인정보 취급위탁을 하는 업무의 내용을 구매자에게 알리고 동의를 받아야 합니다. (동의를 받은 사항이 변경되는 경우에도 같습니다.) 다만, 서비스제공에 관한 계약이행을 위해 필요하고 구매자의 편의증진과 관련된 경우에는 「정보통신망 이용촉진 및 정보보호 등에 관한 법률」에서 정하고 있는 방법으로 개인정보 취급방침을 통해 알림으로써 고지절차와 동의절차를 거치지 않아도 됩니다.\n");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}

