package com.example.luke_imagevideo_send.main.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.http.base.BaseActivity;
import com.example.luke_imagevideo_send.http.views.Header;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgreeActivity extends BaseActivity {

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.header)
    Header header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tv1.setText("\t尊敬的用户，欢迎使用由济宁鲁科检测器材有限公司（下列简称为“鲁科”）提供的服务。在使用前请您阅读如下服务协议，使用本应用即表示您同意接受本协议，本协议产生法律效力，特别涉及免除或者限制鲁科责任的条款，请仔细阅读。如有任何问题，可向鲁科咨询。\n" +
                "\n" +
                "1. 服务条款的确认和接受\n" +
                "\t通过访问或使用本应用，表示用户同意接受本协议的所有条件和条款。\n" +
                "\n" +
                "2. 服务条款的变更和修改\n" +
                "\t鲁科有权在必要时修改服务条款，服务条款一旦发生变更，将会在重要页面上提示修改内容。如果不同意所改动的内容，用户可以放弃获得的本应用信息服务。如果用户继续享用本应用的信息服务，则视为接受服务条款的变更。本应用保留随时修改或中断服务而不需要通知用户的权利。本应用行使修改或中断服务的权利，不需对用户或第三方负责。\n" +
                "\n" +
                "3. 用户行为\n" +
                "3.1 用户账号、密码和安全\n" +
                "\t用户一旦注册成功，便成为本程序的合法用户，将得到一个密码和帐号。同时，此账号密码可登录本APP。因此用户应采取合理措施维护其密码和帐号的安全。用户对利用该密码和帐号所进行的一切活动负全部责任；由该等活动所导致的任何损失或损害由用户承担，鲁科不承担任何责任。 用户的密码和帐号遭到未授权的使用或发生其他任何安全问题，用户可以立即通知鲁科。对于用户长时间未使用的帐号，鲁科有权予以关闭并注销其内容。\n" +
                "\n" +
                "3.2 账号注册时的禁止行为\n" +
                "（1）请勿以党和国家领导人或其他社会名人的真实姓名、字号、艺名、笔名注册；\n" +
                "（2）冒充任何人或机构，或以虚伪不实的方式谎称或使人误认为与任何人或任何机构有关的名称；\n" +
                "（3）请勿注册和其他网友之名相近、相仿的名字；\n" +
                "（4）请勿注册不文明、不健康名字，或包含歧视、侮辱、猥亵类词语的名字；\n" +
                "（5）请勿注册易产生歧义、引起他人误解的名字；\n" +
                "\n" +
                "3.3 用户在本应用上不得发布下列违法信息和照片：\n" +
                "（1）反对宪法所确定的基本原则的；\n" +
                "（2）危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；\n" +
                "（3）损害国家荣誉和利益的；\n" +
                "（4）煽动民族仇恨、民族歧视，破坏民族团结的；\n" +
                "（5）破坏国家宗教政策，宣扬邪教和封建迷信的；\n" +
                "（6）散布谣言，扰乱社会秩序，破坏社会稳定的；\n" +
                "（7）散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的；\n" +
                "（8）侮辱或者诽谤他人，侵害他人合法权益的；\n" +
                "（9）含有法律、行政法规禁止的其他内容的；\n" +
                "（10）禁止骚扰、毁谤、威胁、仿冒网站其他用户；\n" +
                "（11）严禁煽动非法集会、结社、游行、示威、聚众扰乱社会秩序；\n" +
                "（12）严禁发布可能会妨害第三方权益的文件或者信息，例如（包括但不限于）：病毒代码、黑客程序、软件破解注册信息。\n" +
                "（13）禁止上传他人作品。其中包括你从互联网上下载、截图或收集的他人的作品；\n" +
                "（14）禁止上传广告、横幅、标志等网络图片；\n" +
                "\n" +
                "4. 上传或发布的内容\n" +
                "用户上传的内容是指用户在本app上传或发布的视频或其它任何形式的内容包括文字、图片、音频等。作为内容的发表者，需自行对所发表内容负责，因所发表内容引发的一切纠纷，由该内容的发表者承担全部法律及连带责任。鲁科不承担任何法律及连带责任。\n" +
                "鲁科有权（但无义务）自行拒绝或删除经由本应用提供的任何内容。\n" +
                "个人或单位如认为鲁科存在侵犯自身合法权益的内容，应准备好具有法律效应的证明材料，及时与鲁科取得联系，以便鲁科迅速作出处理。");

        tv2.setText("1.用户信息公开情况说明\n" +
                "尊重用户个人隐私是鲁科的一项基本政策。所以，鲁科不会在未经合法用户授权时公开、编辑或透露其注册资料及保存在本应用中的非公开内容，除非有下列情况：\n" +
                "（1）有关法律规定或鲁科合法服务程序规定；\n" +
                "（2）在紧急情况下，为维护用户及公众的权益；\n" +
                "（3）为维护鲁科的商标权、专利权及其他任何合法权益；\n" +
                "（4）其他需要公开、编辑或透露个人信息的情况；\n" +
                "在以下（包括但不限于）几种情况下，鲁科有权使用用户的个人信息：\n" +
                "（1）鲁科可以将用户信息与第三方数据匹配；\n" +
                "（3）鲁科会通过透露合计用户统计数据，向未来的合作伙伴、广告商及其他第三方以及为了其他合法目的而描述鲁科的服务；\n" +
                "\n" +
                "2.隐私权政策适用范围\n" +
                "（1）用户在登录本应用服务器时留下的个人身份信息；\n" +
                "（2）用户通过本应用服务器与其他用户或非用户之间传送的各种资讯；\n" +
                "（3）本应用与商业伙伴共享的其他用户或非用户的各种信息；\n" +
                "\n" +
                "3.资讯公开与共享\n" +
                "鲁科不会将用户的个人信息和资讯故意透露、出租或出售给任何第三方。但以下情况除外：\n" +
                "（1）用户本人同意与第三方共享信息和资讯;\n" +
                "（2）只有透露用户的个人信息和资讯，才能提供用户所要求的某种产品和服务;\n" +
                "\n" +
                "4.账户删除申请\n" +
                "用户有权在任何时候编辑用户在鲁科的帐户信息和资讯，用户也可以填写相关申请表格，要求删除个人帐户，但是用户无条件同意在你的帐户删除后，该帐户内及与该帐户相关的信息和资讯仍然保留在本网站档案记录中，除上述第三条规定的情况外，鲁科将为用户保密。");
        Intent intent = getIntent();
        String tag = intent.getStringExtra("tag");
        if (tag.equals("tv1")) {
            header.setTvTitle(getResources().getString(R.string.agree1));
        } else if (tag.equals("tv2")) {
            header.setTvTitle(getResources().getString(R.string.agree2));
        }
        if (tag.equals("tv1")) {
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.GONE);
        } else if (tag.equals("tv2")) {
            tv1.setVisibility(View.GONE);
            tv2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_agree;
    }

    @Override
    protected boolean isHasHeader() {
        return true;
    }

    @Override
    protected void rightClient() {

    }

    @OnClick(R.id.tv3)
    public void onViewClicked() {
        Uri uri = Uri.parse(tv3.getText().toString());
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(uri);
        startActivity(intent);
    }
}