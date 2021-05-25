package com.example.googledrive;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {

    private  final Executor mExecutor = Executors.newSingleThreadExecutor();
    private Drive mDriveService;

    public DriveServiceHelper(Drive mDriveService)
    {
        this.mDriveService=mDriveService;

    }

    public Task<String> createFilePDF(String filePath)
    {
        return Tasks.call(mExecutor,() ->
                {
                    File file=new File();
                    file.setName("My PDF File");
                    java.io.File file1=new java.io.File(filePath);

                    FileContent mediaContent =new FileContent("application/pdf",file1);
                    File myFile =null;
                    try{
                        myFile =mDriveService.files().create(file,mediaContent).execute();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    if (myFile==null){
                        throw new IOException("Null result");

                    }
                    return myFile.getId();
                });
    }
}
