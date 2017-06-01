package requester;

import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import productinfo.ProductInfo;
import products.Products;

public class Requester {
    String URL = "http://192.168.43.62:8081/setproduct/products.wsdl";
    String NAMESPACE = "http://vlad.com/product";
    String SOAP_ACTION = "http://vlad.com/product/getProductRequest";
    String METHOD_NAME = "getProductRequest";
    String PARAMETER_NAME = "requset";

    public Requester()
    {

    }
    public ProductInfo[] getProductInfo(String id)
    {
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://192.168.43.62:8081/productinfo/"+id);
        ProductInfo productInfo[] = null;
        try
        {
            httpClient.executeMethod(getMethod);
            try
            {
                productInfo = new Gson().fromJson(getMethod.getResponseBodyAsString(),ProductInfo[].class);
            }
            catch (JsonParseException e)
            {
                e.printStackTrace();
                productInfo = new ProductInfo[]{};
            }
        }
        catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productInfo;
    }
    public Products[] getAll()
    {
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://192.168.43.62:8081/products");
        Products products[] = null;
        try
        {
            httpClient.executeMethod(getMethod);
            try
            {
                products = new Gson().fromJson(getMethod.getResponseBodyAsString(),Products[].class);
                System.out.println("response: "+getMethod.getResponseBodyAsString());
            }
            catch (JsonParseException e)
            {
                e.printStackTrace();
                products = new Products[]{};
            }
        }
        catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }
    public boolean delete(long id) {
        String uri = "http://192.168.43.62:8081/products/"+String.valueOf(id);
        HttpClient httpClient = new HttpClient();
        DeleteMethod deleteMethod = new DeleteMethod(uri);
        try {
            httpClient.executeMethod(deleteMethod);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean deleteInfo(long id)
    {
        String uri = "http://192.168.43.62:8081/productinfo/"+String.valueOf(id);
        HttpClient httpClient = new HttpClient();
        DeleteMethod deleteMethod = new DeleteMethod(uri);
        try {
            httpClient.executeMethod(deleteMethod);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String useSoap(String param) {
        String result = "fail";

        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME);

        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.setName(PARAMETER_NAME);
        propertyInfo.setValue(param);
        propertyInfo.setType(String.class);

        soapObject.addProperty(propertyInfo);

        SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call(SOAP_ACTION, envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive)envelope.getResponse();
            result = soapPrimitive.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
