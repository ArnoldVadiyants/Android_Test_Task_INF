package com.infostroytest.rss;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.infostroytest.rss.model.Feed;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class XmlRequest extends Request<Feed> {
    private final Response.Listener<Feed> feedListener;
    private Context context;
    public XmlRequest(Context context, int method, String url, Response.Listener<Feed> feedListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.feedListener = feedListener;
        this.context = context;
    }

    @Override
    protected Response<Feed> parseNetworkResponse(NetworkResponse response) {
        InputStream inputStream = new ByteArrayInputStream(response.data);
        XmlParser parser = XmlParser.newInstance(context, inputStream);
        try {

            parser.parse();
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
        return Response.success(Feed.get(context), HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(Feed response) {
        feedListener.onResponse(response);
    }
}
