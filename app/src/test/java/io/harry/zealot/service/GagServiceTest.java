package io.harry.zealot.service;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.harry.zealot.BuildConfig;
import io.harry.zealot.TestZealotApplication;
import io.harry.zealot.helper.FirebaseHelper;
import io.harry.zealot.model.Gag;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class GagServiceTest {
    @Inject
    FirebaseHelper mockFirebaseHelper;

    @Mock
    DatabaseReference mockDatabaseReference;
    @Mock
    StorageReference mockStorageReference;
    @Mock
    ServiceCallback<List<String>> mockStringListServiceCallback;
    @Mock
    ServiceCallback<List<Uri>> mockUriListServiceCallback;
    @Mock
    StorageReference firstFile;
    @Mock
    StorageReference secondFile;
    @Mock
    Task<StorageMetadata> mockFirstTask;
    @Mock
    Task<StorageMetadata> mockSecondTask;

    @Captor
    ArgumentCaptor<ValueEventListener> valueEventListenerCaptor;
    @Captor
    ArgumentCaptor<OnSuccessListener<StorageMetadata>> onSuccessListenerCaptor;

    private GagService subject;

    @Before
    public void setUp() throws Exception {
        ((TestZealotApplication) RuntimeEnvironment.application).getZealotComponent().inject(this);
        MockitoAnnotations.initMocks(this);

        subject = new GagService(RuntimeEnvironment.application);

        when(mockFirebaseHelper.getDatabaseReference(anyString())).thenReturn(mockDatabaseReference);
    }

    @Test
    public void getGagImageFileNames_getGagsReferenceFromFirebaseHelper() throws Exception {
        subject.getGagImageFileNames(mockStringListServiceCallback);

        verify(mockFirebaseHelper).getDatabaseReference("gags");
    }

    @Test
    public void getGagImageFileNames_addSingleValueEventListenerToReference() throws Exception {
        when(mockFirebaseHelper.getDatabaseReference("gags")).thenReturn(mockDatabaseReference);

        subject.getGagImageFileNames(mockStringListServiceCallback);

        verify(mockDatabaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));
    }

    @Test
    public void getGagImageFileNames_extractsFileNamesFromDataSnapshot() throws Exception {
        DataSnapshot mockDataSnapshot = createMockDataSnapshotForJPGImages(3);

        subject.getGagImageFileNames(mockStringListServiceCallback);

        verify(mockDatabaseReference).addListenerForSingleValueEvent(valueEventListenerCaptor.capture());

        valueEventListenerCaptor.getValue().onDataChange(mockDataSnapshot);

        verify(mockStringListServiceCallback).onSuccess(Arrays.asList("0.jpg", "1.jpg", "2.jpg"));
    }

    @Test
    public void getGagImageUris_getsStorageReferenceFromFirebaseHelper() throws Exception {
        subject.getGagImageUris(new ArrayList<String>(), mockUriListServiceCallback);

        verify(mockFirebaseHelper).getStorageReference("gags");
    }

    @Test
    public void getGagImageURLs_getFilesFromStorageReference() throws Exception {
        createMockStorageReferenceForTwoFiles("0.jpg", "1.jpg");

        subject.getGagImageUris(Arrays.asList("0.jpg", "1.jpg"), mockUriListServiceCallback);

        verify(mockStorageReference).child("0.jpg");
        verify(mockStorageReference).child("1.jpg");
    }

    @Test
    public void getGagImageURLs_getsMetadataFromFile() throws Exception {
        createMockStorageReferenceForTwoFiles("0.jpg", "1.jpg");

        subject.getGagImageUris(Arrays.asList("0.jpg", "1.jpg"), mockUriListServiceCallback);

        verify(firstFile).getMetadata();
        verify(secondFile).getMetadata();
    }

    @Test
    public void getGagImageURLs_addSuccessListenerOnFileMetaData() throws Exception {
        createMockStorageReferenceForTwoFiles("0.jpg", "1.jpg");

        subject.getGagImageUris(Arrays.asList("0.jpg", "1.jpg"), mockUriListServiceCallback);

        verify(mockFirstTask).addOnSuccessListener(Matchers.<OnSuccessListener<StorageMetadata>>any());
        verify(mockSecondTask).addOnSuccessListener(Matchers.<OnSuccessListener<StorageMetadata>>any());
    }

    @Test
    public void getGagImageURLs_successListenerRunsServiceCallbackWithImageURL() throws Exception {
        createMockStorageReferenceForTwoFiles("0.jpg", "1.jpg");

        subject.getGagImageUris(Arrays.asList("0.jpg", "1.jpg"), mockUriListServiceCallback);

        verify(mockFirstTask).addOnSuccessListener(onSuccessListenerCaptor.capture());
        verify(mockSecondTask).addOnSuccessListener(onSuccessListenerCaptor.capture());

        StorageMetadata mockStorageMetaData0 = createMockStorageMetaData("http://myhost/0.jpg");
        onSuccessListenerCaptor.getAllValues().get(0).onSuccess(mockStorageMetaData0);

        verify(mockUriListServiceCallback, never()).onSuccess(anyListOf(Uri.class));

        StorageMetadata mockStorageMetaData1 = createMockStorageMetaData("http://myhost/1.jpg");
        onSuccessListenerCaptor.getAllValues().get(1).onSuccess(mockStorageMetaData1);

        verify(mockUriListServiceCallback).onSuccess(
                Arrays.asList(
                        Uri.parse("http://myhost/0.jpg"),
                        Uri.parse("http://myhost/1.jpg")));
    }

    @NonNull
    private DataSnapshot createMockDataSnapshotForJPGImages(int size) {
        DataSnapshot mockDataSnapshot = mock(DataSnapshot.class);
        List<DataSnapshot> children = new ArrayList<>();

        for(int i = 0; i < size; ++i) {
            DataSnapshot mockChild = mock(DataSnapshot.class);

            Gag gag = new Gag();
            gag.fileName = i + ".jpg";

            when(mockChild.getValue(Gag.class)).thenReturn(gag);

            children.add(mockChild);
        }

        when(mockDataSnapshot.getChildren()).thenReturn(children);

        return mockDataSnapshot;
    }

    private void createMockStorageReferenceForTwoFiles(String firstFileName, String secondFileName) {
        when(mockFirebaseHelper.getStorageReference(anyString())).thenReturn(mockStorageReference);

        when(mockStorageReference.child(firstFileName)).thenReturn(firstFile);
        when(mockStorageReference.child(secondFileName)).thenReturn(secondFile);

        when(firstFile.getMetadata()).thenReturn(mockFirstTask);
        when(secondFile.getMetadata()).thenReturn(mockSecondTask);
    }

    private StorageMetadata createMockStorageMetaData(String url){
        StorageMetadata mockStorageMetaData = mock(StorageMetadata.class);
        Uri mockUri = Uri.parse(url);
        when(mockStorageMetaData.getDownloadUrl()).thenReturn(mockUri);

        return mockStorageMetaData;
    }
}