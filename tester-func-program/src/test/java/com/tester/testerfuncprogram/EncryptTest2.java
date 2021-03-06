package com.tester.testerfuncprogram;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.Security;

public class EncryptTest2 {


    private byte[] getAesKey() throws UnsupportedEncodingException {
//        return "5d6s3a2e5r6f9d6f5d6s3a2e5r6f9d6f".getBytes("GBK");
        return Hex.decode("5d6s3a2e5r6f9d6f5d6s3a2e5r6f9d6f");
    }
    private byte[] getOffset(){
        // 偏移量为16位
        return Hex.decode("00000000000000000000000000000000");
    }
    private byte[] getMessage(){
        return "i hold the key.人交多少积分3827489*@&（&%（@）*".getBytes(Charset.forName("GBK"));
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    @Test
    public void test_sap_encrypt() throws Exception {
        byte[] message = getMessage();
        System.out.println("message.length:" + message.length);

        // PKCS5Padding
        // PKCS7Padding
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(getAesKey(), "AES");
        IvParameterSpec iv = new IvParameterSpec(getOffset());
        System.out.println("偏移量长度：iv.getIV().length:"+iv.getIV().length);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

        // 加密
        byte[] encrypted = cipher.doFinal(message);
        System.out.println("密文："+ new String(Hex.encode(encrypted),"GBK"));
    }
    @Test
    public void test_sap_decrypt() throws Exception {
        byte[] message = Hex.decode(getMsgFromSAP());
        // PKCS5Padding
        // PKCS7Padding
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(getAesKey(), "AES");
        IvParameterSpec iv = new IvParameterSpec(getOffset());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

        // 加密
        byte[] encrypted = cipher.doFinal(message);
        System.out.println(new String(encrypted,"GBK"));
    }
    private String getMsgFromSAP(){
        String res = "bd3d1ffeb985724e03ad4f4f10296d8a5e262bfa71b655175fe694b1838621aa7c3d81b71dca7b55464b6b7bc6d94dd2";
//        String res = "33C044286AED33705E9091918B0F62C6F1A670F9B51872FA3114164D615F4DA6F56E8A7977FBDDAE8B24D35290923011FC89C99F2C31BE18EA891802CD1977546D356A4B54271CA8A4519F65632032A9F44123D4B7948C2C2F954D746D6199A37061955F661CC5B0629715BA335CA1E8AF7C05802AA934829788925448DB97083418F54FB92AC7CA7FD6BDEB20467CB51F4D8B3D7F4183AE40DD54CB2E80F819E1AE49F0675EAD69EB0E9A42E374A560E48C6C129120F39882FAF8B716C3CC8972BF893B73F22ECE786257F39CE4171A82D264C7C5088664BACACE7BE2EF43716701D93C646FE0DA83627F09379D269E9E7FF2628AEA29D44DD5412A4B066B50295C9C0A580BC58506C3F89999C0B20470510CF7A4753329815D47D01CB95464B3A3AC49C2B505D62CF2F8EE81EDDD24C393C94B3C2F1B16BAB3D24C1AFD3BE49365E1E6978AE2822FADEC60B603125E7FE1A6CAC3428C6C672C3A96ECEB5D8EB43F49C05F4BEC273DC24AC56AEA1B1CBCDC8334BC4DF80A70C992C113711DFEAFFDE496D3F0A97DE4E4BBF2620548BCF5559A4BE6D916F8D5A9627F5C7CCAA34C95C599C2F1A56FF2871DCB183E382868CC0D0EC0E1CDC1873A1822AC8B55EDA042EC4E68A0EC5826845A1093920C80B67A94F61D4EB6D7DA2AD0D3E37ED808802FF3CE8DFE8D454189716F7534DFAF4EBF9D5AB1010E5BA7955D73ACC127381A45D4C441C05B3AA7F1B6FAC945C0EE293D4ACFC19B24AE8FE7CE11B038CB3A41EF61318FC661D30B104824352E5096D236586F9B6713CFE113ED10724D3EB7C71012A0A47FCBA8CB2BECB715A2B997ADE31A19C14F93DEC939037C4023DFF433F8202FD122BF336A4812CB93FF1BA4ADD9C9D74E735C3CF8BA3A8C37774C73519FC203FFBB8AFE75FAF34FCBE71EDF1E33842B4FEF7BBCB3D55264CD3240B10D029B6A30AEE75BDA0C2B2AA9376EFE4B6232291E015C70961B163BC248C1A60D601684F47E899629BDE33023E8604BE47297E6400E3A8EB407C8BE14B7872D43F9BCAF29124957A9235504F1588A9DA08607B04AF218AB4084309D7E23DFBD718921577D91950565B96CECE9DB30FDF572EB8F3E3ECBC2687BC918BC828A1515F40CAFCD65FF1D720758D6363087BA8123660501C919F4A94C8701DC048F54EA42C4EC4A8962B2232D65C64F289EEBF49915D4490CC6EDBC9723F5D3524DD34E55D3638F712876455473100268F6A487F725A67ECB099027708EE2A19A6EEC7C003B7138530C640EB4A7B84342E9FA2F9EA1A7CD875AB3B52B892FB488C08FACC93FC6EDA134C4E2CD18F0B1CF267AA2FAFD676485A61884B9876CC5D623E1050D4C7132DE731904A178C36390E791B0CC0EE1A315CFAC754080AE11CD01FFA97CE48A954A833E38D161B1AA436A8AC41D7CBD69CCE1CFC2907C4BD35C28D30EF487940A736503D58C52A8B37EBAB50BD3A3E85820345E7CDDD01B65A34E9106159328D16A5121289EA14ABB3C60711436FED6DCB8AAA733D182183A852AB0E0BC36689204C6F60B4768CF4456BC6B295CF1071F8ADD246CD08127DE78443D956F73F77653D5BC4241C3B41293E0F115135849C2ADEB2D10460E0A8F642E733D7356DD0D0C14D2F4690EC753A92E2BF3D3830BD2C2E13B0BE38BEDBB76CAAEB72D9398AD576D4B1F2CC3EDD40584EDD8A763518B73DE80BB9629BCA46ADD8AA5259B4A3082D9C257F1BE6A82C02798961E87E19E0D45CBFDF2344E575D15A80B4630F6EE2F41C11D9D70F8144AD9F5DC8087254B9903ED4C940DAE1C2215223C274D3BA5F3E828C23FC9433480E5C9AC8B665064B9C21011265226992EB23BAA6C3EDACB6882C374A6E06C3843DE72B192607B302D1EDB43D54F737A932437882430B35A3A7261A98B6FF1E3DEE999E6501DF888EC1964C131C0F2676429F3D5DD640630B83C33CCF876F6F4FBAE170A45008BCAB0D8FA91FB9B5F76DFE9C2CA7EE59583863EDC0C911AE6B51D9D2C3D9373BC9F598705DB7FED72E60B3C89ECB59F189E09E359C9B81603F79CA86869090325F34279AF01B654B0D864DB8298037C35818156E9C150DDDC6C0E0263CB880FFCC815E2106190684A1890C6E0B61AA7FC9C8BE9D7D004A827DCE2ECD84E33CB161C8FC9532FE883B2BB4E4B662F430A56782BAE0822B1DD677D3FBCF7A97B38284E111B9790070F0D01E402116FAC9D72E10D0759D75E245B096D8065AEC5EEC7E60491E4FF1D857692AFB9903C0782558DC79A43637B3B32DA7C67EDF85A518181F6A1E09DC0C455AB92137ABE18894006925BAAC03E0E3881169DB36989C79D1BB1980F3197A238C283C87150315FDAAA95AC5D60853037ED17799CCCECDFEDFC003AC7A5D235C16A197FCF76D914B4D8C688F62BFEE47A383539EE90D97A32F0A4FF591FC344161540E6AAFAFAC69649F77F8E988AE9BC606D1F9A9F64A0D71A8BD6C8D0212E6645180CBB1E421CAB5B1C3A483936119ECE14E639461AD0426B26630914C339E719C1F24396DAA155EE95AB22112DEF61667DEEB20C55579D85104E76B719EF9C9017722858F54C647FAEB6AAA7211FD295E79A045759FA994E7BEA2A9A4B6F3A9501B017A9924EE848FCF0D59E51AED850216FD8387E4D449A994B26F6BB412CEFC46983EB24AEFEE8592A7F05DDD1ABF5F6D0D7B2D5193FA9CAF773E7C5DBAAF2A04F0818011D50F34C817367991923EBBEDADEE052952A8E28771DD458F5AD80A633AE9B3F9B4D3AC9F378B6537F022F22612F7EABCC0E7418510C58417CC9B638D9B08B119AED16D1B1684EDE7963B74305E7C9BF29492FE2F971C6606D62C82D08AB47D87868035153956A0AC80C1384ABA16C08EC69BF0364A9BD634887FBD456DEF07A4F2F44F9F524EB53A21DE440A6E4601E667835136005681F26977B8433099C06E799B7506898BF9F95006131484CACDD89E4334FB1DC084651940FF7A270B5120AE1A2938E0A54A144BB52AE262626D25036DF44321E874A5EC14CAB10D0D46811B307C729D9D44CBC538D4A3193FA1E01AD57F06FA0F1024913B470CE7B0996BDC38A0DA9EDF9800F96A1FF3AA795006353A6750B2109CFEFD135B2CF407CBA3CEE2FFAA974E89FE2014B7251D971F2221830242250986712CDBA2D17D96B84D0C973D5CB00BE96DBD6189B0B6C581CED7C5209BBEDFB56C835B6FD743213690F3E6B6BF9B12ECDC5AE85C9C4465CBE3D3B392820A8A41C89115174B65FE6D6E64953B8E3E0BD78BD7FAE5670A39D756E147400ADBE398AE67CE239581632537FA610648438C3D9409E0B45F5AD5D6273899688DC3DE471CE4F1B56687BA311C2B34FDD3F979719F045981A129258B917EADAF0FFDD8FE338CC5296120C27CF98B8D3F0E81706BD5A76A065CF5CCC9D6B687376B353FF9687C29E46C67F8D1DEC2125D7AAFA1E1B3A761BA49F7D2CC28E254F0CA0F83519AD6FFFBCA9C7C84034666E081EDB51CB5E88B0B709D29F051159DC2070B2155FAFE69D60E1694A430D14CAA6A097D544E94FDE72220937608C6F4945B6AE0D6796B6C6BBC3311BFAD57BDA7E2143674ABFEB22FF66C0CD632B263E8B254F17A9F4A715716CEFAB71CEE36392F3FE53864E77E01C00EF05FCC221336A0E2F4B0ABF8E1B890CB219B9D97833912252D42C9C8595D318CE682F1DF6A9A4A5703646EC1FFA001D2DD768279429B7DCAE09BF323D7F0E35B48C369BC7979D2E2A5F62BF7893B8D24FBF2FC483864F05A5ADAC84C950F03AC5385C16C6D49EE52471260FF2573FD812AE9E2E565ED4CBD36832D5523314343C63F53508D6D48785A95711BB6FE3B07E";
        return res;
    }
}
