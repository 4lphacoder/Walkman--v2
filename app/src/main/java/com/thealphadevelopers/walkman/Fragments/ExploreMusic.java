//package com.thealphadevelopers.walkman.Fragments;
//
//import android.content.Context;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.util.TypedValue;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.content.res.ResourcesCompat;
//import androidx.fragment.app.Fragment;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.thealphadevelopers.walkman.Models.Youtube.Item;
//import com.thealphadevelopers.walkman.Models.Youtube.Search;
//import com.google.gson.Gson;
//import com.thealphadevelopers.walkman.MPState;
//
//import org.json.JSONObject;
//import org.w3c.dom.Text;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class ExploreMusicFragment extends Fragment {
//
//    // DECLARING OBJECTS
//    private EditText searchBar;
//    private LinearLayout searchResultLayout;
//    private LinearLayout exploreActivityBtn;
//    private LinearLayout mainActivityBtn;
//    private LinearLayout searchBarParent;
//    private LinearLayout parentNode;
//    private ImageView searchBarClearButton;
//    private ScrollView searchResultScrollView;
////    private Status status;
////    private MinimalMC mediaController;
////    private SearchItemLayout searchItemLayout;
//    private Context context;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.d("Application-Status","[ INFO  ] ExploreMusicFragment onCreate Method Called");
////        View exploreMusicView = inflater.inflate(R.layout.explore_music_fragment,container,false);
//
//        // FETCHING ELEMENTS FROM XML
////        searchBar = exploreMusicView.findViewById(R.id.search_bar);
////        searchBarParent = exploreMusicView.findViewById(R.id.searchbar_parent);
////        searchResultLayout = exploreMusicView.findViewById(R.id.search_result_layout);
////        exploreActivityBtn = exploreMusicView.findViewById(R.id.explore_btn);
////        mainActivityBtn = exploreMusicView.findViewById(R.id.home_btn);
////        parentNode = exploreMusicView.findViewById(R.id.search_result);
////        searchResultScrollView = exploreMusicView.findViewById(R.id.search_result_scrollbar);
////        searchBarClearButton = exploreMusicView.findViewById(R.id.search_bar_clear_btn);
//
//        // CREATING INSTANCE OF SOME USEFUL CLASSES
////        status = new Status(context,getActivity());
////        mediaController = new MinimalMC(context,getActivity());
////        searchItemLayout = new SearchItemLayout(context,getActivity(),getActivity().getSupportFragmentManager());
//
//        // REQUESTING FOCUS ON SEARCH-BOX
//        searchBar.requestFocus();
//
//        // ADDING CLICK LISTENER ON SEARCH BAR CLEAR BUTTON
//        searchBarClearButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                searchBar.setText(new String());    // SETTING EMPTY STRING
//                searchBar.requestFocus();
//            }
//        });
//
//        // ADDING FOCUS CHANGE LISTENER ON SEARCH-BOX
//        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    inputMethodManager.showSoftInput(searchBar,InputMethodManager.SHOW_IMPLICIT);
//                }
//            }
//        });
//
//        // ADDING TEXT WATCHER ON SEARCH-BOX
//        searchBar.addTextChangedListener(new TextWatcher() {
//            // CREATING TIMER OBJECT SO THAT VOLLEY REQUESTS API AFTER GIVEN DELAY
//            private Timer timer = new Timer();
//            private final long DELAY = 700;
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // DO NOTHING
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                searchResultScrollView.smoothScrollTo(0,0);
//                String SearchQuery = s.toString();
//                if(SearchQuery.length() != 0) {
//                    searchResultLayout.setVisibility(View.GONE);
////                    status.setLoaderLayout();
//                    searchBarClearButton.setVisibility(View.VISIBLE);
//                }
//                else {
//                    // HIDING SEARCH LIST FROM SEARCH ACTIVITY TO MAKE ROOM AVAILABLE TO DISPLAY
//                    // TRENDING SONGS
//                    searchResultLayout.setVisibility(View.GONE);
////                    status.removeActivityStatusLayout();
//                    searchBarClearButton.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                final String SearchQuery = s.toString();
//                // CANCELLING PREVIOUSLY SCHEDULED TIMER TO RUN TASK
//                timer.cancel();
//
//                // CREATING NEW TIMER AND WAIT FOR DELAY milliSeconds TO QUERY API
//                // IN ORDER TO SAVE MULTIPLE REQUESTS SENT OVER API
//                timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        // QUERY YOUTUBE FOR THE GIVEN SEARCH RESULT
//                        if(SearchQuery.length() != 0) {
//                            searchYoutubeWithKeyword(SearchQuery,25);
//                        }
//                    }
//                },DELAY);
//                //
//            }
//        });
//
////        return exploreMusicView;
//    }
//
//    // THIS METHOD INPUTS KEYWORD TO SEARCH AND MAX-RESULTS TO SHOW
//    private void searchYoutubeWithKeyword(String str, int MaxResult) {
//        String URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=<MAX_RESULTS>&q=<QUERY_STRING>&key=<API_KEY>";
//        URL = URL.replace("<QUERY_STRING>",str);
////        URL = URL.replace("<API_KEY>", Config.getYoutubeDataApiKey());
//        URL = URL.replace("<MAX_RESULTS>", String.valueOf(MaxResult));
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        // CREATING REQUEST OBJECT STARTS HERE
//        JsonObjectRequest requestObject = new JsonObjectRequest(Request.Method.GET,
//                URL, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // PARSING DATA FROM JSON STRING TO JAVA CLASS OBJECTS
//                        Gson gson = new Gson();
//                        Search youtubeSearchResp = gson.fromJson(response.toString(),Search.class);
//                        displaySearchResults(youtubeSearchResp);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // THIS METHOD RUNS WHEN VOLLEY ENCOUNTER ANY ERROR WHILE PERFORMING ANY
//                        // SEARCH QUERY
//                        Log.v("Application-Status","[ Error ] " + error.toString() );
//                        if( error.networkResponse != null
//                                && (error.networkResponse.statusCode == 400
//                                || error.networkResponse.statusCode == 401
//                                || error.networkResponse.statusCode == 403 ) ) {
////                            Config.changeCurrentYoutubeDataApiKey();
//                        }
//                        // SHOWING NETWORK ERROR TO USER AND GUIDING HIM/HER FURTHER
////                        status.setNetworkErrorLayout();
//                    }
//                }
//        );
//        // JSON REQUEST OBJECT ENDS HERE
//        // SETTING RETRY POLICY OF JSON REQUEST OBJECT TO AVOID SENDING OF SAME REQUEST
//        // MULTIPLE TIMES
//        requestObject.setRetryPolicy(
//                new DefaultRetryPolicy(20*1000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        // ADDING JSON REQUEST OBJECT IN REQUEST QUEUE
//        requestQueue.add(requestObject);
//    }
//
//    // THIS METHOD INPUTS OBJECT OF SEARCH CLASS AND INSERT THE SEARCH RESULT ONTO THE SPECIFIED
//    // CONTAINER
//    private void displaySearchResults(Search youtubeSearchResp) {
//        MPState.lastYoutubeSearchResp = youtubeSearchResp;     // STORING SEARCH-RESP IN GLOBAL-VARIABLE
//        // FOR RECREATING UI WHEN USER PAUSE THIS ACTIVITY
////        status.removeActivityStatusLayout();
//        searchResultLayout.setVisibility(View.VISIBLE);
//        // CREATING XML ELEMENTS THAT NEEDS TO DISPLAY DYNAMICALLY
//        parentNode.removeAllViews();
//
//        for(int idx=0; idx < youtubeSearchResp.getItems().size() ; idx++) {
//            final Item searchRespItem = youtubeSearchResp.getItems().get(idx);
//            if( searchRespItem.getId().getKind().equals("youtube#video") ) {
//                // THIS BLOCK EXECUTES WHEN WE RECEIVE A VIDEO LINK FROM YOUTUBE SEARCH
//                // CREATING IMAGEVIEW, TEXT VIEW FOR DISPLAYING SEARCH RESULT ON SEARCH_MUSIC_ACTIVITY
////                parentNode.addView(searchItemLayout.generateResource(searchRespItem));
//                if( parentNode.getChildCount() == 1) {
//                    TextView songs = new TextView(context);
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
//                            (LinearLayout.LayoutParams.MATCH_PARENT,
//                                    LinearLayout.LayoutParams.WRAP_CONTENT);
////                    layoutParams.setMargins((int)getResources().getDimension(R.dimen.dimen_16dp),
////                            (int)getResources().getDimension(R.dimen.dimen_16dp),
////                            (int)getResources().getDimension(R.dimen.dimen_16dp),
////                            (int)getResources().getDimension(R.dimen.dimen_4dp));
//                    songs.setText("Songs");
//                    songs.setLayoutParams(layoutParams);
////                    songs.setTypeface(ResourcesCompat.getFont(context,R.font.gotham_bold));
//                    songs.setTextSize(TypedValue.COMPLEX_UNIT_SP,26);
////                    songs.setTextColor(getResources().getColor(R.color.colorWhite));
//                    parentNode.addView(songs);
//                }
//            }
//        }
//        // ADDING INVISIBLE BLOCK TO COUNTER BOTTOM APP BARS
//        View invisibleBlock = new View(context);
//        invisibleBlock.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
////                (int) getResources().getDimension(R.dimen.dimen_150dp)));
//        parentNode.addView(invisibleBlock);
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        Log.d("Application-Status","[ INFO  ] ExploreMusicFragment onAttach Method Called");
//        super.onAttach(context);
//        this.context = context;
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.d("Application-Status","[ INFO  ] ExploreMusicFragment onPause Method Called");
//        // STORING SCROLL POSITION TO RE-CREATE THIS LAYOUT ON-RESUME CALLED
//        MPState.exploreMusicScrollX = searchResultScrollView.getScrollX();
//        MPState.exploreMusicScrollY = searchResultScrollView.getScrollY();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d("Application-Status","[ INFO  ] ExploreMusicFragment onResume Method Called");
//        if ( MPState.lastYoutubeSearchResp != null ) {
//            displaySearchResults(MPState.lastYoutubeSearchResp);
//            searchResultScrollView.smoothScrollTo(MPState.exploreMusicScrollX, MPState.exploreMusicScrollY);
//            searchBar.clearFocus();
//        }
//        else {
//            // SETTING UP THE VIEW FOR FIRST INSTANCE RUN
//            searchBar.requestFocus();
//            InputMethodManager inputMethodManager = (InputMethodManager)
//                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        Log.d("Application-Status","[ INFO  ] ExploreMusicFragment onDestroy Method Called");
//        super.onDestroy();
//    }
//}
//
