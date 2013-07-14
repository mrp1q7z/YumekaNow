/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yojiokisoft.yumekanow.activity;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Spinner;

import com.google.billing.util.IabHelper;
import com.google.billing.util.IabResult;
import com.google.billing.util.Purchase;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.yojiokisoft.yumekanow.R;
import com.yojiokisoft.yumekanow.adapter.KeyValueArrayAdapter;
import com.yojiokisoft.yumekanow.adapter.KeyValueArrayAdapter.Converter;

/**
 * Example game using in-app billing version 3.
 * 
 * Before attempting to run this sample, please read the README file. It
 * contains important information on how to set up this project.
 * 
 * All the game-specific logic is implemented here in MainActivity, while the 
 * general-purpose boilerplate that can be reused in any app is provided in the 
 * classes in the util/ subdirectory. When implementing your own application,
 * you can copy over util/*.java to make use of those utility classes.  
 * 
 * This game is a simple "driving" game where the player can buy gas
 * and drive. The car has a tank which stores gas. When the player purchases
 * gas, the tank fills up (1/4 tank at a time). When the player drives, the gas
 * in the tank diminishes (also 1/4 tank at a time).
 *
 * The user can also purchase a "premium upgrade" that gives them a red car
 * instead of the standard blue one (exciting!).
 * 
 * The user can also purchase a subscription ("infinite gas") that allows them
 * to drive without using up any gas while that subscription is active.
 *
 * It's important to note the consumption mechanics for each item.
 *
 * PREMIUM: the item is purchased and NEVER consumed. So, after the original
 * purchase, the player will always own that item. The application knows to
 * display the red car instead of the blue one because it queries whether
 * the premium "item" is owned or not.
 * 
 * INFINITE GAS: this is a subscription, and subscriptions can't be consumed.
 *
 * GAS: when gas is purchased, the "gas" item is then owned. We consume it
 * when we apply that item's effects to our app's world, which to us means
 * filling up 1/4 of the tank. This happens immediately after purchase!
 * It's at this point (and not when the user drives) that the "gas"
 * item is CONSUMED. Consumption should always happen when your game
 * world was safely updated to apply the effect of the purchase. So,
 * in an example scenario:
 *
 * BEFORE:      tank at 1/2
 * ON PURCHASE: tank at 1/2, "gas" item is owned
 * IMMEDIATELY: "gas" is consumed, tank goes to 3/4
 * AFTER:       tank at 3/4, "gas" item NOT owned any more
 *
 * Another important point to notice is that it may so happen that
 * the application crashed (or anything else happened) after the user
 * purchased the "gas" item, but before it was consumed. That's why,
 * on startup, we check if we own the "gas" item, and, if so,
 * we have to apply its effects to our world and consume it. This
 * is also very important!
 *
 * @author Bruno Oliveira (Google)
 */
@EActivity(R.layout.activity_billing)
public class BillingActivity extends Activity {
	// Debug tag, for logging
	static final String TAG = "TrivialDrive";

	// SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
	static final String SKU_DONATION_100 = "donation_100";
	static final String SKU_DONATION_300 = "donation_300";
	static final String SKU_DONATION_500 = "donation_500";
	static final String SKU_DONATION_700 = "donation_700";
	static final String SKU_DONATION_1100 = "donation_1100";
	static final String SKU_DONATION_1300 = "donation_1300";
	static final String SKU_DONATION_1700 = "donation_1700";

	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;

	@ViewById(R.id.donationType)
	/*package*/Spinner mDonationType;

	// The helper object
	IabHelper mHelper;

	private String getPublicKey() {
		String str[] = { "MIIBIjANBgkqhkiG", "w", "BAQEFAAOCAQ", "AMIIBCgKCAQEAnXiDtgDV+JvMJx", "6CitVY", "jS",
				"3/zje", "lHRA/WqU", "2JgaOZCvBCk", "NnNK", "yuXZfCuUwn", "aVNV", "G", "t", "uwiXP/zPuGJ", "mADsLAH",
				"nb", "k", "uhh", "EDDHK", "PuaCYguhgTaYyw", "U+", "fKAP", "/ZY", "8Ut+BFR", "H", "ojkjFzzuZsoRjGOJ",
				"tF", "FZi", "nQDUdZoEdCXyeRhGf/POKlcDWQy", "pj+wn", "NqE", "gp", "R", "MKiGKC", "auA",
				"amBupBCm//KJqe", "dvl", "VT0", "fXhj", "ol+0", "H", "EwvdRbSSwZ", "Cu", "P", "ogrEV", "lYcG/",
				"AgPRjNr0", "T", "fnEgjiNSCbXZ", "w", "JOnB/fh", "nzhQKA", "G", "wIDAQA"
		};
		int num[] = {
				9, 0, 8, 1014, 7, 1011, 9, 1010, 0, 1, 0, 4, 650, 1, 6, 36, 9, 0, 7, 5, 8, 1, 4, 1012, 13, 4, 2, 330,
				62, 3, 1, 3, 5, 4, 4, 5, 1, 1, 0, 7, 9, 6, 7, 9, 8, 1, 3, 9, 4, 2, 41, 6, 6, 2, 1011
		};
		StringBuilder sb = new StringBuilder();
		int len = str.length;
		for (int i = 0; i < len; i++) {
			sb.append(str[i]);
			if (num[i] > 1000) {
				String hex = Integer.toHexString(num[i] - 1000).toUpperCase(Locale.ENGLISH);
				sb.append(hex);
			} else {
				sb.append(num[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * アクティビティの初期化 (onCreateと同等のタイミングで呼ばれる）
	 */
	@AfterViews
	/*package*/void initActivity() {
		String base64EncodedPublicKey = getPublicKey();

		KeyValueArrayAdapter<Pair<String, String>> adapter = new KeyValueArrayAdapter<Pair<String, String>>
				(this, R.layout.item_donation_list, new Converter<Pair<String, String>>() {
					@Override
					public String toDisplayString(Pair<String, String> t) {
						return t.second;
					}

				});
		adapter.setDropDownViewResource(R.layout.row_donation_list);

		adapter.add(new Pair<String, String>(SKU_DONATION_100, "１００円の寄付"));
		adapter.add(new Pair<String, String>(SKU_DONATION_300, "３００円の寄付"));
		adapter.add(new Pair<String, String>(SKU_DONATION_500, "５００円の寄付"));
		adapter.add(new Pair<String, String>(SKU_DONATION_700, "７００円の寄付"));
		adapter.add(new Pair<String, String>(SKU_DONATION_1100, "１１００円の寄付"));
		adapter.add(new Pair<String, String>(SKU_DONATION_1300, "１３００円の寄付"));
		adapter.add(new Pair<String, String>(SKU_DONATION_1700, "１７００円の寄付"));

		mDonationType.setAdapter(adapter);

		// Create the helper, passing it our context and the public key to verify signatures with
		Log.d(TAG, "Creating IAB helper.");
		mHelper = new IabHelper(this, base64EncodedPublicKey);

		// enable debug logging (for a production application, you should set this to false).
		mHelper.enableDebugLogging(true);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					complain("Problem setting up in-app billing: " + result);
					return;
				}
			}
		});
	}

	/**
	 * 寄付するボタンのクリック
	 */
	@Click(R.id.donationButton)
	/*package*/void donationButtonClicked() {
		Log.d(TAG, "donation button clicked.");

		// launch the gas purchase UI flow.
		// We will be notified of completion via mPurchaseFinishedListener
		setWaitScreen(true);
		Log.d(TAG, "Launching purchase flow for donation.");

		/* TODO: for security, generate your payload here for verification. See the comments on 
		 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use 
		 *        an empty string, but on a production app you should carefully generate this. */
		String payload = "";

		Pair<String, String> selectedItem = (Pair<String, String>) mDonationType.getSelectedItem();
		String sku = selectedItem.first;
		Log.d(TAG, "sku=" + sku);
		mHelper.launchPurchaseFlow(this, sku, RC_REQUEST, mPurchaseFinishedListener, payload);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		}
		else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/*
		 * TODO: verify that the developer payload of the purchase is correct. It will be
		 * the same one that you sent when initiating the purchase.
		 * 
		 * WARNING: Locally generating a random string when starting a purchase and 
		 * verifying it here might seem like a good approach, but this will fail in the 
		 * case where the user purchases an item on one device and then uses your app on 
		 * a different device, because on the other device you will not have access to the
		 * random string you originally generated.
		 *
		 * So a good developer payload has these characteristics:
		 * 
		 * 1. If two different users purchase an item, the payload is different between them,
		 *    so that one user's purchase can't be replayed to another user.
		 * 
		 * 2. The payload must be such that you can verify it even when the app wasn't the
		 *    one who initiated the purchase flow (so that items purchased by the user on 
		 *    one device work on other devices owned by the user).
		 * 
		 * Using your own server to store and verify developer payloads across app
		 * installations is recommended.
		 */

		return true;
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
			if (result.isFailure()) {
				complain("Error purchasing: " + result);
				setWaitScreen(false);
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				complain("Error purchasing. Authenticity verification failed.");
				setWaitScreen(false);
				return;
			}

			Log.d(TAG, "Purchase successful. sku=" + purchase.getSku());

			Log.d(TAG, "Purchase is gas. Starting gas consumption.");
			mHelper.consumeAsync(purchase, mConsumeFinishedListener);
		}
	};

	// Called when consumption is complete
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

			// We know this is the "gas" sku because it's the only one we consume,
			// so we don't check which sku was consumed. If you have more than one
			// sku, you probably should check...
			if (result.isSuccess()) {
				// successfully consumed, so we apply the effects of the item in our
				// game world's logic, which in our case means filling the gas tank a bit
				Log.d(TAG, "Consumption successful. Provisioning.");
				alert("ご寄付いただき、心より感謝いたします。ありがとうございました。");
			}
			else {
				complain("Error while consuming: " + result);
			}
			setWaitScreen(false);
			Log.d(TAG, "End consumption flow.");
		}
	};

	// We're being destroyed. It's important to dispose of the helper here!
	@Override
	public void onDestroy() {
		super.onDestroy();

		// very important:
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
		}
		mHelper = null;
	}

	// Enables or disables the "please wait" screen.
	void setWaitScreen(boolean set) {
		findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
		findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);
	}

	void complain(String message) {
		Log.e(TAG, "**** TrivialDrive Error: " + message);
		alert("Error: " + message);
	}

	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d(TAG, "Showing alert dialog: " + message);
		bld.create().show();
	}
}
