package com.ryx.ethan.fingerlessbrowser;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.PeopleDet;
import com.tzutalin.dlib.VisionDetRet;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static android.hardware.camera2.CameraCharacteristics.LENS_FACING;
import static android.hardware.camera2.CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP;
import static android.hardware.camera2.CameraMetadata.CONTROL_AF_STATE_ACTIVE_SCAN;
import static android.hardware.camera2.CameraMetadata.LENS_FACING_FRONT;
import static android.hardware.camera2.CaptureRequest.CONTROL_AF_MODE;


/**
 * Created by ethan on 4/26/2017.
 */

public class BlinkDetectFragment extends Fragment {
    OnBlinkListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        return null;
    }

    public interface OnBlinkListener {
        void onBlinked(int count);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBlinkListener) {
            mCallback = (OnBlinkListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet MyListFragment.OnItemSelectedListener");
        }
    }

    /*@Override
    public void setRetainInstance() {
        //stub do I need this??
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public void updateDetail(String uri) {
        // create a string just for testing
        String newTime = String.valueOf(System.currentTimeMillis());

        // inform the Activity about the change based
        // interface defintion
        mCallback.onBlinked(5);
    }

    //get camera image
    private static final int sImageFormat = ImageFormat.YUV_420_888;

    private String mCameraId;
    private AutoFitTextureView mTextureView;
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener =
            new ImageReader.OnImageAvailableListener() {

                @Override
                public void onImageAvailable(ImageReader reader) {
                    Log.e(TAG, "onImageAvailable: " + count++);
                    Image img = null;
                    img = reader.acquireLatestImage();
                    Result rawResult = null;
                    try {
                        if (img == null) throw new NullPointerException("cannot be null");
                        ByteBuffer buffer = img.getPlanes()[0].getBuffer();
                        byte[] data = new byte[buffer.remaining()];
                        buffer.get(data);
                        int width = img.getWidth();
                        int height = img.getHeight();
                        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, width, height);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                        //do find facial landmarks
                        findEyes(bitmap);
                    } catch (ReaderException ignored) {
                        Log.e(TAG, "Reader shows an exception! ", ignored);
                        /* Ignored */
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    } finally {
                        mQrReader.reset();
                        Log.e(TAG, "in the finally! ------------");
                        if (img != null)
                            img.close();

                    }
                    if (rawResult != null) {
                        Log.e(TAG, "Decoding successful!");
                    } else {
                        Log.d(TAG, "No QR code found…");
                    }
                }

            };

    //blink detction
    public void findEyes(Bitmap videoFrame) {
        PeopleDet peopleDet = new PeopleDet();
        List<VisionDetRet> results = peopleDet.detBitmapFace(videoFrame, Constants.getFaceShapeModelPath());
        for (final VisionDetRet ret : results) {
            String label = ret.getLabel(); // If doing face detection, it will be 'Face'
            int rectLeft = ret.getLeft();
            int rectTop= ret.getTop();
            int rectRight = ret.getRight();
            int rectBottom = ret.getBottom();
            ArrayList<Point> landmarks = ret.getFaceLandmarks();
            for (Point point : landmarks) {
                int pointX = (int) (point.x);
                int pointY = (int) (point.y);
                // Get the points for two eyes
            }
        }
    }


}
