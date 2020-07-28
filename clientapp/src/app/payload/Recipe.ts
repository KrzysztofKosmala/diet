import {Product} from "./Product";

export class Recipe
{
    name: String;
    description: String;
    type: String;
    kcal: number;

    multiplier: number;
    products: Array<Product>;

}
