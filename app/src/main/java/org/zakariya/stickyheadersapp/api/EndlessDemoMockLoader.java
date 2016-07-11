package org.zakariya.stickyheadersapp.api;

import android.os.Handler;

import java.util.ArrayList;

/**
 * This is a fake service for async loading data for use by EndlessScrollDemoActivity
 */
public class EndlessDemoMockLoader {

	public static class ItemModel {
		String title;

		public ItemModel(String title) {
			this.title = title;
		}

		public String getTitle() {
			return title;
		}
	}

	public static class SectionModel {
		String title;
		ArrayList<ItemModel> items = new ArrayList<>();

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public ArrayList<ItemModel> getItems() {
			return items;
		}

		public void setItems(ArrayList<ItemModel> items) {
			this.items = items;
		}

		public void addItem(ItemModel itemModel) {
			items.add(itemModel);
		}
	}

	public interface Listener {
		void onSectionLoadBegun();
		void onSectionLoadProgress(float progress);
		void onSectionLoaded(SectionModel sectionModel);
	}

	int vendCount = 0;
	Handler handler;
	Runnable runnable;
	Listener listener;
	int tick;
	static final int STEPS = 100;
	static final long DELAY = 20;

	public EndlessDemoMockLoader() {
	}

	public void load(final Listener listener) {

		this.listener = listener;

		if (handler == null) {
			handler = new Handler();
		}

		if (runnable == null) {
			runnable = new Runnable() {
				@Override
				public void run() {
					tick++;
					if (tick < STEPS) {
						stepLoad();
					} else {
						finishLoad();
					}
				}
			};
		}


		tick = 0;
		listener.onSectionLoadBegun();
		stepLoad();
	}

	/**
	 * Build a section and vend it synchronously
	 * @return a new populated SectionModel
	 */
	public SectionModel vendSection() {
		EndlessDemoMockLoader.SectionModel sectionModel = new EndlessDemoMockLoader.SectionModel();
		sectionModel.setTitle("Page " + Integer.toString(vendCount));

		for (int j = 0; j < 20; j++) {
			sectionModel.addItem(new EndlessDemoMockLoader.ItemModel("Item " + Integer.toString(j)));
		}

		vendCount++;
		return sectionModel;
	}

	private void stepLoad() {
		listener.onSectionLoadProgress((float) tick / (float) STEPS);
		handler.postDelayed(runnable, DELAY);
	}

	private void finishLoad() {
		listener.onSectionLoaded(vendSection());
	}


}
