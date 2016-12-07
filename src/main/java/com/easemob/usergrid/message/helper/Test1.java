package com.easemob.usergrid.message.helper;

import org.apache.hadoop.fs.Path;

/**
 * Created by zhouhu on 15/10/2016.
 */
public class Test1 {
    public static void main(String[] args) {
        Path path = new Path("/tmp/rds002/org_app_2016101502.gz");
        System.out.println("path name = " + path.getName());

    }
}
