# PopEditText
在聊天界面中，一般底部是一个EditText，点击EditText旁边的语音图标或者表情图标都可以弹出底部的一些小工具。但在此过程中，键盘的弹出或者隐藏表现的不尽友好。有时候显示或者隐藏键盘的时候，底部的工具Container View总是往上弹一下然后再下去。这个demo演示了如何让Keyboard结合EditText隐藏或者显示的更平滑一些。

In a word, this demo is aim to demostrate how to pop or hide keyboard with EditText in a more elegant way.

![Demonstrate](https://github.com/anxiaoyi/PopEditText/blob/master/PopEditText/popedittext.gif)

Download Demo: [Download](https://github.com/anxiaoyi/PopEditText/blob/master/PopEditText/PopEditText.apk)

## How to use:
```
mPopEditText.setContainer("The bottom container which holds the views that you want to show.");
mButton.setOnClickListener(new View.OnClickListener(){ 
	@Override
	public void onClick(View v){ 
		mPopEditText.dispatchOnClickEvent(v, "The view that you want to show when user click view v.");
	}
});
```

## Contact me:
1. QQ:1291700520
2. Email:igozhaokun@163.com