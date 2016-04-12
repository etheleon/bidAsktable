/**
 * Created by uesu on 12/4/16.
 */


import java.net.*;
import java.nio.*;
import java.awt.*;
import java.text.ParseException;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*; //required for TableCellRenderer
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;





public class bidAskTable extends JFrame{
    private byte bb;

    private static final long serialVersionUID = 1L;
    // Instance attributes used in this example
    private	JPanel  topPanel;
    private	JTable  table;
    private	JScrollPane scrollPane;


    private bidAskTable() {
        setTitle( "Stocks ask bid price" );
        setSize( 300, 200 );
        setBackground( Color.gray );

        // Create a panel to hold all other components
        topPanel = new JPanel();
        topPanel.setLayout( new BorderLayout() );
        getContentPane().add( topPanel );
//        Object[] columnNames = { "ID", "Time", "Bid", "Ask", "Size", "data"}; //6 Fields
        Object[] columnNames = { "ID", "Bid", "Ask"}; //6 Fields
        Object[][] data =
        {
                { "abc" , new Double(850.503)  , 53 }   ,
                { "lmn" , new Double(36.23254) , 6  }   ,
                { "pqr" , new Double(8.3)      , 7  }   ,
                { "xyz" , new Double(246.0943) , 23 }
        };

        table = new JTable(data, columnNames) {
            private static final long serialVersionUID = 1L;

//            @Override
//            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
//                Component comp = super.prepareRenderer(renderer, row, col);
//                int modelRow = convertRowIndexToModel(row);
//                int modelColumn = convertColumnIndexToModel(col);
//                //Object value = getModel().getValueAt(row, col);
//                if (!isRowSelected(modelRow)) {
//                    //if (modelColumn != 0 || modelRow != 0) {
//                    comp.setBackground(table.getBackground());
//                } else {
//                    comp.setBackground(Color.RED);
//                    comp.setForeground(Color.BLACK);
//                }
//                //}
//                return comp;
//            }
        };


        // Add the table to a scrolling pane
        scrollPane = new JScrollPane( table );
        topPanel.add( scrollPane, BorderLayout.CENTER );
    }

//    public class Producer implements Runnable {
//
//        protected BlockingQueue queue = null;
//
//        public Producer(BlockingQueue queue) {
//            this.queue = queue;
//        }
//
//
//    }
//
//    public class Consumer implements Runnable {
//
//        protected BlockingQueue queue = null;
//
//        public Consumer(BlockingQueue queue) {
//            this.queue = queue;
//        }
//
//        public void run() {
//            try {
//                System.out.println(queue.take());
//                System.out.println(queue.take());
//                System.out.println(queue.take());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    public static void main( String args[] ) throws Exception
    {
        InetAddress group = InetAddress.getByName("224.5.6.7");
        MulticastSocket s = new MulticastSocket(1234);
        s.joinGroup(group);

        byte[] buf = new byte[4000];


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                bidAskTable app = new bidAskTable();
                app.setVisible(true);
            }
        });

        //
//        BlockingQueue queue = new ArrayBlockingQueue(1024);
//
//        Producer producer = new Producer(queue);
//        Consumer consumer = new Consumer(queue);
//
//        new Thread(producer).start();
//        new Thread(consumer).start();
//
//        Thread.sleep(4000);


        while(true)
        {
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            s.receive(recv);
            //System.out.println("Received: " + new String(recv.getData(), 0, recv.getLength()));
            ByteBuffer b = ByteBuffer.wrap(recv.getData());
            b.order(ByteOrder.LITTLE_ENDIAN);
            System.out.println("First: "    + (char) b.get());
            System.out.println("ID: "       + b.getInt());
            System.out.println("Time: "     + b.getLong());
            System.out.println("Bid: "      + b.getDouble());
            System.out.println("Ask: "      + b.getDouble());
            System.out.println("Size: "     + b.getInt());
            byte[] array = new byte[b.get()];
            b.get(array);
            System.out.println("data: " + new String(array));
        }
    }
}