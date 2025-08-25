package cl.smapdev.curimapu.fragments.dialogos;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutionException;

import cl.smapdev.curimapu.R;

public class FragmentQRScanner extends Fragment {

    private PreviewView previewView;
    private ProcessCameraProvider cameraProvider;
    private boolean isScanned = false;

    public interface OnScanResultListener {
        void onScanResult(String result);
    }

    private OnScanResultListener resultListener;

    public void setOnScanResultListener(OnScanResultListener listener) {
        this.resultListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        previewView = view.findViewById(R.id.previewView);

        Button btnClose = view.findViewById(R.id.btnCloseScanner);
        btnClose.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        startCamera();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;


                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                BarcodeScanner scanner = BarcodeScanning.getClient();

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext()), imageProxy -> {
                    @SuppressLint("UnsafeOptInUsageError")
                    Image mediaImage = imageProxy.getImage();
                    if (mediaImage != null && !isScanned) {
                        InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
                        scanner.process(image)
                                .addOnSuccessListener(barcodes -> {
                                    for (Barcode barcode : barcodes) {
                                        String rawValue = barcode.getRawValue();
                                        if (rawValue != null && !isScanned) {
                                            isScanned = true;
                                            if (resultListener != null) {
                                                resultListener.onScanResult(rawValue);
                                            }

                                            requireActivity().getSupportFragmentManager().popBackStack();
                                            break;
                                        }
                                    }
                                })
                                .addOnCompleteListener(task -> imageProxy.close());
                    } else {
                        imageProxy.close();
                    }
                });

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                preview.setTargetRotation(Surface.ROTATION_0);


            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }
}