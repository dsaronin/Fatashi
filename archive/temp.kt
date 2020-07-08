viewPager.post {
  viewPager.setCurrentItem(1, true)
}


 To make it work you have to define a callback, here is an example:

ViewPager2.OnPageChangeCallback callback = new ViewPager2.OnPageChangeCallback() {
    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
    }
};

And then you have to register it as follow:

viewPager.registerOnPageChangeCallback(callback);
Also do not forget to unregister it:

viewPager.unregisterOnPageChangeCallback(callback);

When you call setCurrentItem(position) method it will call onPageSelected(int position) method from 
your callback passing your argument, and then method createFragment(int position) from 
FragmentStateAdapter class will be called to show your fragment.

