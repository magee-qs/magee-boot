import hasPerm from "./hasPerm";
import hasRole from "./hasRole";

export default function directive(app){
    app.directive('hasRole', hasRole);
    app.directive('hasPerm', hasPerm);
}