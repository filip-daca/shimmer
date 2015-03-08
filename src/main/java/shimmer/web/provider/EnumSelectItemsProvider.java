package shimmer.web.provider;

import java.util.Arrays;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Named;

import shimmer.domain.helper.NamesHelper;
import shimmer.enums.Metric;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * @author filip.hubert.daca@gmail.com
 */
@Named
public class EnumSelectItemsProvider {

	// POLA ------------------------------------------------------------------------------------- /
	
    private List<SelectItem> metrics = toSelectItems(Metric.values());
    private List<SelectItem> sizeMetrics = toSelectItems(Metric.sizeMetrics());
    private List<SelectItem> colorMetrics = toSelectItems(Metric.colorMetrics());
    private List<SelectItem> heatMetrics = toSelectItems(Metric.heatMetrics());

    // METODY GŁÓWNE ---------------------------------------------------------------------------- /
    
    private static List<SelectItem> toSelectItems(Enum<?>... values) {
        return Lists.newArrayList(Lists.transform(Arrays.asList(values), new Function<Enum<?>, SelectItem>() {
            @Override
            public SelectItem apply(Enum<?> input) {
                return selectItemFromEnum(input);
            }
        }));
    }
    
    @SuppressWarnings("unused")
	private List<SelectItem> toSelectItemsWithAllOption(Enum<?>... values) {
		List<SelectItem> items = toSelectItems(values);
		items.add(0, new SelectItem("", "All"));
		return items;
	}


	private static SelectItem selectItemFromEnum(Enum<?> e) {
        return new SelectItem(e, NamesHelper.enumToNiceString(e));
    }

    // GETTERY / SETTERY ------------------------------------------------------------------------ /
    
    public List<SelectItem> getMetrics() {
		return metrics;
	}
    
    public List<SelectItem> getColorMetrics() {
		return colorMetrics;
	}
    
    public List<SelectItem> getHeatMetrics() {
		return heatMetrics;
	}
    
    public List<SelectItem> getSizeMetrics() {
		return sizeMetrics;
	}
}
