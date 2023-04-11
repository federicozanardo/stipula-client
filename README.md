# Example of an application interacting with a *Stipula server* or a *Stipula node*

The project presented here represents a possible application that is able to interact with a *Stipula server* or *Stipula node*.
In this case, the application can:
- Upload a contract
- Make function calls
- Request the available funds of a specific address

This project could also serve as a starting point for the creation of a **wallet** (see issue https://github.com/federicozanardo/stipula-node/issues/8).

This code can interact independently with a *Stipula server* or a *Stipula node* because it is necessary to interact only with the *Message Service* module.

## Examples

In the current version of the project, only three sample contracts are executed:
- AssetSwap
- AssetSwapWithEvent
- BikeRental

## References

Stipula implementation: https://github.com/federicozanardo/stipula-node

Thesis: 