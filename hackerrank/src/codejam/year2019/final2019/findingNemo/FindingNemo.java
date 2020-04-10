package codejam.year2019.final2019.findingNemo;

public class FindingNemo {
    public static void main(String[] args) {
        long len = 100;
        long height = 100;

        long posX = 50;
        long posY = 50;

        long resCnt = 0;

        long cnt = 0;

        for (long xDisp = -posX; posX + xDisp < len; xDisp++) {
            for (long yDisp = -posY; posY + yDisp < height; yDisp++) {
                if (xDisp == 0 && yDisp == 0) {
                    resCnt += len * height;
                } else {
                    long xDisp_ = xDisp;
                    long yDisp_ = yDisp;

                    while ((posX + xDisp_ < len && posX + xDisp_ >= 0) && (posY + yDisp_ < height && posY + yDisp_ >= 0)) {
                        cnt++;
                        resCnt += (len - Math.abs(xDisp_)) * (height - Math.abs(yDisp_));
                        xDisp_ += xDisp;
                        yDisp_ += yDisp;
                    }
                }
            }
        }

        System.out.println(resCnt);
        System.out.println(cnt);
    }
}
