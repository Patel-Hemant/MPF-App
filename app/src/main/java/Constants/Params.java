package Constants;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Params {
    public static final String DATA_TRANSFER_KEY = "data";
    public static final String IMAGE_URL_TRANSFER_KEY = "url";
    public static final String DATABASE_ROOT_KEY = "MissingPersonDataRoot";
    public static final String DATABASE_MESSAGE_KEY = "messages";
    public static final String STORAGE_ROOT_KEY = "uploads";
    public static final String USER_ID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
}
