package de.bsvrz.sys.funclib.bitctrl.modell;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

public abstract class AbstractParameterDatensatz extends AbstractDatensatz
		implements ClientReceiverInterface {

	private Data sendeDaten;

	@Override
	protected void fireAutoUpdate() {
		ClientDavInterface dav = ObjektFactory.getInstanz().getVerbindung();
		DataModel model = dav.getDataModel();

		if (isAutoUpdate()) {
			dav.subscribeReceiver(this, getObjekt().getSystemObject(),
					new DataDescription(getAttributGruppe(), model
							.getAspect(Konstante.DAV_ASP_PARAMETER_SOLL)),
					ReceiveOptions.normal(), ReceiverRole.receiver());
		} else {
			dav.unsubscribeReceiver(this, getObjekt().getSystemObject(),
					new DataDescription(getAttributGruppe(), model
							.getAspect(Konstante.DAV_ASP_PARAMETER_SOLL)));
		}
	}

	public void sendeDaten() {
		// TODO Auto-generated method stub

	}

	public void update(ResultData[] results) {
		for (ResultData result : results) {
			if (result.hasData()) {
				setDaten(result.getData());
				setValid(true);
			} else {
				setValid(false);
			}
		}
	}
}
