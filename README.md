# w4lr-ScoreView
一个支持自定义得分段描述信息与得分信息的自定义的控件
使用：
布局文件中引入

```
<com.w4lr.scoreview.ScoreView
        android:id="@+id/sv"
        android:layout_width="400dp"
        android:layout_height="300dp"/>
```

代码设置得分段和每个得分段的描述信息

```
ScoreView sc = (ScoreView) findViewById(R.id.sv);
        int[] scores = {9,1,1,1,2,3,8,1,2};
        String[] texts = new String[]{"100","90-99","80-89","70-79","60-69","60以下","50","40"};
        sc.setDescTexts(texts);
        sc.setScoreCount(scores);
```
