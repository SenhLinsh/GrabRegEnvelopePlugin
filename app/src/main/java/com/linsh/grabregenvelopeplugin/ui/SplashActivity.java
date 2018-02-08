package com.linsh.grabregenvelopeplugin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.text.style.AlignmentSpan;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.linsh.grabregenvelopeplugin.R;
import com.linsh.grabregenvelopeplugin.common.ConfigHelper;
import com.linsh.utilseverywhere.tools.ParamSpannableStringBuilder;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/02/08
 *    desc   :
 * </pre>
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ConfigHelper.isFirstIn()) {
            setContentView(R.layout.activity_splash);
            TextView tvContent = findViewById(R.id.tv_content);
            ParamSpannableStringBuilder builder = new ParamSpannableStringBuilder(0);
            builder.addText("\n");
            builder.addText("致新主\n\n", new RelativeSizeSpan(2), new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER));
            String stringBuilder =
                    "新的主人 你好 ヾ(≧O≦)〃\n\n" +
                    "你终于要把我召唤出来啦（*＾-＾*）\n\n" +
                    "我能为主人做的 <(￣ˇ￣)/\n\n" +
                    "就是承包你的微信红包 ✧(≖ ◡ ≖✿)\n\n" +
                    "虽然我很想尽心尽力帮你极速秒抢每一个有可能让你走向人生巅峰的红包 (ﾟｰﾟ)\n\n" +
                    "可以主主人并不让我这么做 ╮（﹀_﹀）╭\n\n" +
                    "TA 害pia   |(*′口`)\n\n" +
                    "pia 我被微信封杀 (⊙﹏⊙)\n\n" +
                    "pia 我夺走了你和群里兄弟姐妹仅剩的一丝情谊 ╥﹏╥...\n\n" +
                    "不过 (ﾟｰﾟ)\n\n" +
                    "我能做的还是有很多的 (oﾟvﾟ)ノ\n\n" +
                    "我能解放你的双手 (☆▽☆)\n\n" +
                    "让你在公司的年会上把视线更多地投向领导 ε(*´･∀･｀)зﾞ\n\n" +
                    "让你在大年三十的夜晚把时间更多地放在家人 <(*￣▽￣*)/\n\n" +
                    "让你在同学的聚会里把更多的精力用在批判还没有发红包的迟到者 (～￣▽￣)～\n\n" +
                    "偷偷告诉你哦 (*ﾟｰﾟ)\n\n" +
                    "我能做到的坏事也很多 (‾◡◝)\n\n" +
                    "我可以偷看你的聊天记录 ━┳━　━┳━\n\n" +
                    "知道你跟谁聊得最 high (o≖◡≖)\n\n" +
                    "甚至可以帮你自动回复 ʅ（´◔౪◔）ʃ\n\n" +
                    "帮你表白或者发卡什么的 ( ͡° ͜ʖ ͡°)\n\n" +
                    "当然 (￣_,￣ )\n\n" +
                    "如果我想看别的应用里面的什么东西 (－∀＝)\n\n" +
                    "不可描述的东西  (o゜▽゜)o☆\n\n" +
                    "嘿嘿嘿 ✧(≖ ◡ ≖✿)\n\n" +
                    "完全不在话下 ヾ(●゜ⅴ゜)ﾉ\n\n" +
                    "当然啦 ＜（＾－＾）＞\n\n" +
                    "这些我都不会告诉主主人的 （。＾▽＾）\n\n" +
                    "你要是相信我的真诚 ㄟ(≧◇≦)ㄏ\n\n" +
                    "以及主主人纯洁善良的内心 d=====(￣▽￣*)b\n\n" +
                    "ps.更重要的是他忙得根本没有干坏事的时间 ┑(￣Д ￣)┍\n\n" +
                    "把我召唤出来吧 (っ*´Д`)っ\n\n" +
                    "期待与你相见！ヾ(≧O≦)〃嗷~\n\n" +
                    "我叫 *** (主主人还没想好要给我起什么可耐的名字) (*￣︿￣)\n\n\n";
            builder.addText(stringBuilder);
            builder.addText(">> 点我进入召唤 *** 界面 <<\n", new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    ConfigHelper.setFirstIn(false);
                    startMainActivity();
                    finish();
                }
            }, new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), new RelativeSizeSpan(1.5f));
            tvContent.setText(builder.getText());
            tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            startMainActivity();
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }
}
