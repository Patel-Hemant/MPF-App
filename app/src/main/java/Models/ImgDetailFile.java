package Models;

import android.net.Uri;

public class ImgDetailFile {
    private String mName;
    private Uri mImageUrl;

    public ImgDetailFile() {
    }

    public ImgDetailFile(String name, Uri imageUrl) {
        mName = name;
        mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Uri getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(Uri imageUrl) {
        mImageUrl = imageUrl;
    }
}
