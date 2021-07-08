package com.tester.testercommon.util;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @Author 温昌营
 * @Date 2020-7-2
 */
public class DecimalUtil {


    public static BigDecimal toDecimal(BigDecimal decimal){
        if(null == decimal){
            return BigDecimal.ZERO;
        }
        return decimal;
    }
    /**
     * BigDecimal格式化
     */
    public static String format(BigDecimal value) {
        return format(value,2);
    }
    /**
     * BigDecimal格式化
     */
    public static String format(BigDecimal value, Integer len) {
        if (value == null) {
            return "";
        }
        if (null == len || len < 1) {
            len = 2;
        }
        StringBuilder pattern = new StringBuilder("0.");
        for (int i = 0; i < len; i++) {
            pattern.append("#");
        }
        DecimalFormat decimalFormat = new DecimalFormat(pattern.toString());
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(value);
    }

    public static BigDecimal getScaleAmount(BigDecimal totalAmount) {
        if(totalAmount == null){
            return BigDecimal.ZERO;
        }
        return totalAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public static String numberToString(BigDecimal num) {
        if (num != null) {
            return num.stripTrailingZeros().toPlainString();
        }
        return null;
    }


    /**
     * 将BigDecimal转换为字符串
     */
    public static String bigDecimalToNoZeroStr(BigDecimal num) {
        try {
            if (num == null) {
                return "0";
            }
            return num.stripTrailingZeros().toPlainString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 转换字符串为BigDecimal
     */
    public static BigDecimal strToBigDecimal(String str) {
        if (StringUtils.isEmpty(str)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(str).stripTrailingZeros();
    }

    /**
     * 如果未null 则默认未0
     */
    public static int defaultInt(Integer value) {
        return value == null ? 0 : value;
    }

    /**
     * 如果未null 则默认未0
     */
    public static int defaultInt(BigDecimal value) {
        return value == null ? 0 : value.intValue();
    }

//    // 转换
//    public static void main(String[] args) {
//        String decryptedData = "0ddadbcb02ef0bd1dc06510e93be972f02bbcddf90c0a67ae5998ce6008d628cddf4e75df05b579898d6a0adab5a070cf4fc257a933543de2f38a6557658847761200eb7729722c0e777e163dd8b3e26ec3abeb4b650eb532cc99343471f8fdf4e3afbed85d1d767f29bc880acef3f17d462709a56c37565c35c114d51845effa96be29f20345e3060d40687a7cbe234109f9b35833d6619b67665085722bcae77083097eea97a15d4e309b6ff6d084916d1403b86c43537b6c2c123b8cfb7ad941ac76b291c98e27a3c645e8dcc9b349a90cf64fa12e7fb7bf038383864657231c294b7bfd00f89e10426ef45ecf5e8c8b5afafff743e006eea21790aa274a0aa8e1eadc21192930aa7da3a35a7daf2c1f8e0c9e26e2d44a77a0effe37a02a9a4d9f72d90a7020f5580ca25769fffdc48e1ca11741c81ef58da0456697e1564c95afcd8a7b0cb8ded956406a16d6c6825edbb6e7e5b87c946c02664220b072dcea052186b3275eaad4de9e77f3b08baf5531914cae5a722694d751f9ead01ea1200798109528a64f282fb13b22ee1a8b0a2f1b40e8a6a048d9eb38a0e544909fba0e85b6c3860c40ce1cc554de357b1a814009b71edc3b3cf4dde8e19b99c007a093369f57fdadb9dd0e6ec330f492c71e5aba6df58c8ba4c67a0ceebb77646f2f6411e2bc1522b1d699cf1d9ca60bac847fc10ba7d52ba05fa49edf4810eb4628a26eac71aaa0b6af2d49aae13934d25c6ece7cfd3ea9eabb79869c2c5aa7954a7be4a370135fdd3586f36ae6993f83721506bf6e011e81ca3746dcf320fd912c88d06fd594a052575a0bf706a6b362b5622f6a28db66401e4e60dcca87f5be43693a2212dba4fbd4022772ee20346b1ed978c86ac94cbe064b5a2804d01b1ad1353684fa159cdc11d65b8f42128057475a1f8e7af9c8a29a7a97fe70d048e678482b1d3c0433ddca869cd1fd2e0f7a05f63503cb05ee346efd52ed4d05c09bd42f49093dfb28503c09f3a401f9bd9ffaf0e9a79b989d9d553a39568830968c1f7922b045eee7d4c48c3ac19c7ecf08c3a1fc9b19b26ba17e2250f467ec909b4767cd325a3dbdb44db56d6653291b3b4524b63e99ac62e5d950bf1892c74a2dbcb660cee29d1bbfda72262f9ad0f5d894a495cd2f376cdae9e8f62a575c15f52150f030460187f8c71a50944d17a6c38ed06ce9c35bacd59051a9fb763442eec872dba39b44be47d801a64e6cfd527bdeb38f60253c0f655c80b04be61219f2fa29e10f1dfffd9a6c138c586c8042d128d96509c24d86db571730c2c40ef61fe5db10d428147e93b29ce89098cab7d78b04b36cc3618b63915db7152bb3cc7ccf8ee2a403ec24b0a7ea1c4af45d8f36773a5c3efb818c3168860b0cc0541124f571061ff47b7a762a8358a4988ff50c4023b91cdb959d1fe68db2bf88d48217337a84127ef8a313af54b51972c4253080125b98270c0902a347b404e2b20b9b48b82aa193eec64f4e38eb3d4aeb234b93ca635ca9e903267ecba01019559fe505b427a487b5a415785bc6cbe95fc37a72a4cd4aa461d533d43a1bd1ebdc72af0c9158870afb7e6681cc19b6c79c536c593fe9c453f51d1574d173957350409ac31d1cc57b3965143c9e2d7d37257c01efd6b2c1c84d02dd939a53649edd580a7c200853206a78c15b21eab139a254923d1601c2230aa4835fbeef8f45d4168587a87a3809c08a0382dc60cb34dc578b2d97a75349d596d95e740ca034662a4cc29fdc17323a140b702d21c1a82cd4643bddc1fcaacbd2a53e06b6f17cdc5e293672d79ea12ee40287751a5b216913cd52df284c8a01197de3eaaf3b2220bf67574285f16dd741b4de813cb27e6e1171ed1273129e4df5bcf6c27052096088fabfaf181caaf231b28aa17699fb0589f22b9bd6a8a751cf41bddd00a96645f3ffac8ad964029a188db2cdd34201ef7a79a53414f1b06ff4ceff8548be32beed14f21cb461da3bfde5a9dc7e1110175040b5421bb9f2242cca96f9df961ec74880469298870fe29d714934acc2b3815fe1f4acbb636aaf55dc002d57b91b99bfdc6bba4293360519e91e089d86354a47b29b74fe2661f35bc412ffbf7a524a61838cfc2feec8e8a15a27f1a05d798cfe0b37b8b577708f82dfe1b17481c974b8c95e7e2cc9a4c97ca6cace056dcce39ef587198336e9eb424868206615faff8770ac954ed0de2e14c9995637fb876dcc0c09e5661b1ba25b8ebf3196972e07c1c0d6912c839fde3a40fc23b7e125cf64a3659b76aff0b0b71fd56805c124ab33dacb5295ccdeb49990bdf454a76fb27df911a5853751c0a50b5d095034ba19d98112e85ac7330a0b3643cdaa19d3e93963d21551940218734e0ffd6512797ca71792e010c2e14a1a1d3959e9a078fde041eac934b6c9380362d8bb6d617685781e4afe1ba6cd9366c13c04c7b9e1b527a489e7ad94cdbcf9bde1f0000e1c529420d6ee2be13b79fe16d66d60f3316894257df0776ed5089c700bbbe32b6fa5361090015f4be141a193a5cf26ecc8b35cfacbc4e1178298e9e4875589e3546a0b8b42b97dbeee5780cba03597b1be4895e6aeaa0a27c671c145a6da14fb1417fe14e27506d412c4327e862091a7be497e8d2bab66630b6b093a34ad004e99ac12abf142fbb8bbbdadb9da5444a7e4ea122571256b17bf26830dbc6bd969762cd1b3d8062a96200745eee0cef765f8cc2c0f8e71275496bb9f5b13f4bdc8a328af53c6ae472ea09c912db3930ccea5c81c413d3ef47167cbb4848fbcfd7f33912e1df5a9c5e723c11914a1e42e0d07e7e8ae75c97a31c6df1cee14a1b6dc595b3b2eeeea5c4a7fec9b04da6a066812f275bad7ef08411798223812cb336bac2c8781dedbecb386eb1ce37db75e73d80a22eff8ca13adcd32de91c1c46180bb6d2c0f2a8c30d828c31da81f3b3afbae1755a51024826852e2441f0cf2752a46951c49f5e010aac7e9d09ab7a17416926d8657bd0842ec0a7ed5275e731dbac2419516090a932cd6f461ac7c93afb69e3cb6a4b46760197243834f789fa9304d8666c97359ccdc3c1349e35dd330e825bc33bab3a4e6d244db958ce5dfc73ff278c0bda7492af9ff63622004b0d97b288545b55c4ecab5d1eaf2fa2d604e569c5aad1eaa8a3910b2ca502e84f0ad33b325b3603cd827d51cf2642c16f37d3222042dcc3f3a29a41e8109db060f9c33acc68f0db30c992648a69848a7ed0ce254f4d7f3053155ac1ca5bb3e36b5aaa645b124cca4d9df9d8809dfc075f64486654431f950caf092ffd9faef8f34c3c62d4c8151b3d2d246329329c1a4818750af6cfa73e05e0d0793c53350feb924ebfcf240653f177e3188fef0797cafe9b9682e66c7bc26106c9f203c07418ae437436a70bf77a2476077b1376f910d89385265669";
//        String privateKey = "30820276020100300d06092a864886f70d0101010500048202603082025c02010002818100c25da5b4ff8cc22c15181163d8dc412387ceac072e70149963f1973198b7edb2545636a39f826321408899b5059bce67f40f31cacb8bd218f1cd2cc10f97990badbad247823ea63adac7a5e09a91943ade0b6f1511ba39422e07c379e48f057bcfcf3cbba644fff88faa0df5048febab5f913eb72e54ca371d9e0528158fe949020301000102818061f49a5fd5ed0baaf5d2946912d1264f06d1c4e735a32cee856d2b7ac671e3ec9d0e7a290a69c715ea152e0a0a5294bad548971a926cdc2cd0284c66dc39d5da28195a32b7ab247a0e64000227fb2162c8a6e8be1614f53373ebf488e5a6a9c13a859a1250a19e177679f9f7c7fa70039bd2120e25f4a48f549ba3863189add5024100f4e12ae0118459c9ba2410aec303c3b5e80e32e189eb3306965993c0f94bf0412f8b5a5aa20a065d33591e3ddf07ba57a82550a24d39b8ae846a96d77d70d41b024100cb313c39d7f810358c63c28913b29ac4bbe05852339e9a66a5a4e5c00c5a7cb1de912f91c9611c664176cf991edd59b34cd9e024dc7ee2f79a84ce1f91f5e66b024100e92b5a0aff42f9c7e6911ebd797da52619af236db4489b4e836d0a0eae924b539674e774f363fe495272622461b0d7134e4d31a94945540000a009bf68773dcf02402a3d79ffac5ded40b486a892467e029e934ff80c5e9ec20a44b51aede9a385f894042a976bfdbc21a5ca452dd7b4bc4901d8e937c07ec04028e722d27d4d5931024045c062f19856ceda00609c3d98704fad5883a4771dc742125d1d8f28f068f273c57f29c4ee44846b3ea3d3a54f8d137032486030540bf030501702498a192ed1";
//        String encrypt = RSAEncrypt.decrypt(decryptedData, privateKey);
//        System.out.println("file data is:");
//        System.out.println(encrypt);
//    }
}