package Models;

import android.net.Uri;

public class ImgDetailFile {
    private String mName;
    private Uri mImageUri;

    public ImgDetailFile() {
    }

    public ImgDetailFile(String name, Uri imageUri) {
        mName = name;
        mImageUri = imageUri;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Uri getImageUri() {
        return mImageUri;
    }

    public void setImageUri(Uri imageUri) {
        mImageUri = imageUri;
    }
}
