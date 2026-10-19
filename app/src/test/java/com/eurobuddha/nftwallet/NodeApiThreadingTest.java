package com.eurobuddha.nftwallet;

import android.content.Context;
import android.os.Handler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.mockito.MockedConstruction;
import com.eurobuddha.minimaapi.MinimaAPI;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

public class NodeApiThreadingTest {
    @Test public void workerCannotDispatchUntilTheMainHandlerRuns() throws Exception {
        List<Runnable> posted=Collections.synchronizedList(new ArrayList<>());
        try(MockedConstruction<Handler> handlers=mockConstruction(Handler.class,(h,c)->{
            when(h.post(any())).thenAnswer(i->{posted.add(i.getArgument(0));return true;});
        }); MockedConstruction<MinimaAPI> clients=mockConstruction(MinimaAPI.class)) {
            NodeApi api=new NodeApi(mock(Context.class),null);
            Thread guardian=new Thread(()->api.cmd("status",null)); guardian.start();guardian.join();
            verify(clients.constructed().get(0),never()).Command(anyString(),any());
            assertEquals(1,posted.size());posted.remove(0).run();
            verify(clients.constructed().get(0)).Command(eq("status"),any());
            api.onDestroy();
        }
    }
    @Test public void releaseBeforeQueuedSetupReturnsAnErrorWithoutSubmitting() {
        List<Runnable> posted=new ArrayList<>();
        try(MockedConstruction<Handler> handlers=mockConstruction(Handler.class,(h,c)->{
            when(h.post(any())).thenAnswer(i->{posted.add(i.getArgument(0));return true;});
        }); MockedConstruction<MinimaAPI> clients=mockConstruction(MinimaAPI.class)) {
            NodeApi api=new NodeApi(mock(Context.class),null); NodeApi.Cb callback=mock(NodeApi.Cb.class);
            api.cmd("txnpost id:fixture",callback);api.onDestroy();posted.remove(0).run();posted.remove(0).run();
            verify(clients.constructed().get(0),never()).Command(anyString(),any());
            verify(callback).onError("Node API released");verify(callback,never()).onResult(any());
        }
    }
    @Test public void aLateReplyAfterReleaseCannotReachTheOldScreen() throws Exception {
        java.util.concurrent.atomic.AtomicReference<com.eurobuddha.minimaapi.MinimaAPIListener> reply=new java.util.concurrent.atomic.AtomicReference<>();
        try(MockedConstruction<Handler> handlers=mockConstruction(Handler.class,(h,c)->{
            when(h.post(any())).thenAnswer(i->{((Runnable)i.getArgument(0)).run();return true;});
        }); MockedConstruction<MinimaAPI> clients=mockConstruction(MinimaAPI.class,(api,c)->{
            doAnswer(i->{reply.set(i.getArgument(1));return null;}).when(api).Command(anyString(),any());
        })) {
            NodeApi api=new NodeApi(mock(Context.class),null); NodeApi.Cb callback=mock(NodeApi.Cb.class);
            api.cmd("balance",callback);assertNotNull(reply.get());api.onDestroy();
            reply.get().response(new org.json.JSONObject().put("status",true));
            verifyNoInteractions(callback);
        }
    }
}
