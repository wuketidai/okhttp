package okhttp3;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import okio.BufferedSource;

public class PathologicalClientTest {
  public static void main(String[] args) throws IOException, InterruptedException {
    final ServerSocket s = new ServerSocket(0);

    Thread t = new Thread() {
      @Override public void run() {
        try {
          Socket socket = s.accept();
          OutputStream os = socket.getOutputStream();
          byte[] ys = new byte[8192 * 100];
          Arrays.fill(ys, (byte) 'y');
          while (true) {
            System.err.print(".");
            os.write(ys);
            Thread.sleep(100);
          }
        } catch (IOException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    t.start();

    OkHttpClient c = new OkHttpClient();
    Request req = new Request.Builder().url("http://localhost:" + s.getLocalPort()).build();

    System.out.println("Requesting " + req);

    Response r = c.newCall(req).execute();

    System.out.println("Response " + r.code());

    BufferedSource source = r.body.source();

    source.request(100);
  }
}
