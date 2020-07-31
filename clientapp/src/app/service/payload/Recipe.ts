import {Product} from "./Product";

export class Recipe
{
    name: String;
    description: String;
    type: Array<String>;
    kcal: number;

    multiplier: number;
    products: Array<Product>;

}
