package com.cctv.xiqu.android.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class WeiboUtils {
	public enum Synbol {
		URL, AT, TOPIC
	}

	public static class WeiboSymbol {
		private Synbol symbol;
		private String text;

		public WeiboSymbol(Synbol symbol, String text) {
			super();
			this.symbol = symbol;
			this.text = text;
		}

		public String getText() {
			return text;
		}

		public Synbol getSymbol() {
			return symbol;
		}
	}

	public static class WeiboText {
		private List<WeiboSymbol> symbols = new ArrayList<WeiboSymbol>();
		private String htmlText;

		public WeiboText(String htmlText) {
			super();
			this.htmlText = htmlText;
		}

	}


	
	public static class WeiboSymboResult{
		private ClickableString clickableString;
		private int start;
		private int end;
		public WeiboSymboResult(ClickableString clickableString, int start,
				int end) {
			super();
			this.clickableString = clickableString;
			this.start = start;
			this.end = end;
		}
		public ClickableString getClickableString() {
			return clickableString;
		}
		public int getEnd() {
			return end;
		}
		public int getStart() {
			return start;
		}
		
	}
	

	private static List<WeiboSymboResult> build(Synbol synbol,String text,OnSymbolClickLisenter onSymbolClickLisenter){
		String regex = null;
		switch (synbol) {
		case AT:
			 regex = "@(\\S+)";
			break;
		case TOPIC:
			regex = "#(.*)#";
			break;
		case URL:
			regex = "(https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])";
			break;
		default:
			break;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		List<WeiboSymboResult> list = new ArrayList<WeiboSymboResult>();
		while (matcher.find()) {
			list.add(new WeiboSymboResult(new ClickableString(Color.parseColor("#3498b1"), new WeiboSymbol(synbol, matcher.group(1)), onSymbolClickLisenter), matcher.start(), matcher.end()));
		}
		return list;
	}
	

	public static class ClickableString extends ClickableSpan {

		private int color;

		private WeiboSymbol symbol;

		private OnSymbolClickLisenter onSymbolClickLisenter;

		public ClickableString(int color, WeiboSymbol symbol,
				OnSymbolClickLisenter onSymbolClickLisenter) {
			super();
			this.color = color;
			this.symbol = symbol;
			this.onSymbolClickLisenter = onSymbolClickLisenter;
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			// TODO Auto-generated method stub
			super.updateDrawState(ds);
			ds.setColor(color);
			ds.setUnderlineText(false);
		}

		@Override
		public void onClick(View v) {
			onSymbolClickLisenter.OnSymbolClick(symbol);
		}
	}

	public static interface OnSymbolClickLisenter {
		public void OnSymbolClick(WeiboSymbol symbol);
	}

	public static ArrayList<WeiboSymboResult> build(String text,OnSymbolClickLisenter listener){
		ArrayList<WeiboSymboResult> list = new ArrayList<WeiboSymboResult>();
		list.addAll(build(Synbol.URL, text, listener));
		list.addAll(build(Synbol.AT, text, listener));
		list.addAll(build(Synbol.TOPIC, text, listener));
		return list;
		
	}

}
