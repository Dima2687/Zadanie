package contants;


import org.aeonbits.owner.Config;

@Config.Sources(
        "file:./src/test/resources/config/config.properties"
)
public interface MyConfig extends org.aeonbits.owner.Config {

    @Key("base_url")
    String url();
}
